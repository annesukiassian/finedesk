package tk.finedesk.finedesk.aws.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.exception.SdkServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.utils.IoUtils;
import tk.finedesk.finedesk.aws.configuration.model.AmazonS3SourceKey;
import tk.finedesk.finedesk.aws.configuration.model.Source;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Base64;
import java.util.Objects;

import static java.lang.Integer.MAX_VALUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonS3Service {

    private final S3Client s3Client;

    public boolean putObject(String bucket, String folder, Source report) {
        final AmazonS3SourceKey sourceKey = (AmazonS3SourceKey) report.getSourceKey();
        return putObject(bucket, getObjectKeyWithinFolder(folder, sourceKey.getKey()), sourceKey.getETag(), sourceKey.getSize(), report
                .getContent());
    }

    public boolean putObject(String bucket, String key, String eTag, Long size, InputStream contentInputStream) {
        final AmazonS3ContentMetadata sourceMetadata;
        final RequestBody requestBody;

        try {
            sourceMetadata = getContentMetadata(contentInputStream);
            if (size != null && size != 0) {
                requestBody = RequestBody.fromInputStream(contentInputStream, size);
            } else {
                requestBody = RequestBody.fromInputStream(contentInputStream, sourceMetadata.contentLength);
            }
        } finally {
            closeContentInputStream(contentInputStream);
        }

        var putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)

                .contentEncoding("UTF-8")
                .contentMD5(Objects.isNull(eTag) ? sourceMetadata.messageDigest : eTag)
                .build();

        final PutObjectResponse putObjectResponse;
        try {
            putObjectResponse = s3Client.putObject(putObjectRequest, requestBody);
            return putObjectResponse.sdkHttpResponse().isSuccessful();
        } catch (SdkClientException | SdkServiceException e) {
            log.debug("Unable to put s3 object: bucket {} and key {}. Reason: {}", bucket, key, e);
            return false;
        }
    }

    private String getObjectKeyWithinFolder(String folder, String key) {
        return folder + "/" + key;
    }

    public String getPresignedUrl(S3Presigner s3Presigner, String bucketName, String fileName) {


        try {

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            GetObjectPresignRequest objectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofHours(1))
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(objectPresignRequest);

            URL url = presignedGetObjectRequest.url();
            String path = url.getPath();

            log.info(path);


            log.info("Url {} : ", url);

            HttpURLConnection httpURLConnection = (HttpURLConnection) presignedGetObjectRequest.url().openConnection();

            presignedGetObjectRequest.httpRequest()
                    .headers()
                    .forEach((header, values) -> {
                        values.forEach(value -> {
                            httpURLConnection.addRequestProperty(header, value);
                        });
                    });

            if (presignedGetObjectRequest.signedPayload().isPresent()) {
                httpURLConnection.setDoOutput(true);

                try (InputStream signedPayload = presignedGetObjectRequest.signedPayload().get().asInputStream();
                     OutputStream httpOutputStream = httpURLConnection.getOutputStream()) {
                    IoUtils.copy(signedPayload, httpOutputStream);
                }
            }

            try (InputStream content = httpURLConnection.getInputStream()) {
//                System.out.println("Service returned response: ");
//                IoUtils.copy(content, System.out);
            }

            return path;
        } catch (S3Exception | IOException e) {
            log.info(e.getMessage());
        }
        return null;
    }


    private static class AmazonS3ContentMetadata {
        private final String messageDigest;
        private final int contentLength;

        public AmazonS3ContentMetadata(String messageDigest, int contentLength) {
            this.messageDigest = messageDigest;
            this.contentLength = contentLength;
        }
    }


    private AmazonS3ContentMetadata getContentMetadata(final InputStream source) {
        DigestInputStream digestInputStream = wrapIntoDigestInputStream(source);

        int contentLength = calculateContentLength(digestInputStream);
        String messageDigestStr = encodeMessageDigest(digestInputStream);

        return new AmazonS3ContentMetadata(messageDigestStr, contentLength);
    }


    private DigestInputStream wrapIntoDigestInputStream(InputStream inputStream) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError("JVM doesn't support MD5!", e);
        }
        return new DigestInputStream(inputStream, md);
    }


    private int calculateContentLength(DigestInputStream digestInputStream) {
        final int byteBufferSize = 1024;
        byte[] byteBuffer = new byte[byteBufferSize];
        int contentLength = 0;

        digestInputStream.mark(MAX_VALUE);

        try {
            int readBytes = digestInputStream.read(byteBuffer);
            while (readBytes > 0) {
                contentLength = contentLength + readBytes;
                readBytes = digestInputStream.read(byteBuffer);
            }

            digestInputStream.reset();
        } catch (IOException exception) {
            throw new RuntimeException("Unable to read from content input stream.", exception);
        }

        return contentLength;
    }

    private String encodeMessageDigest(DigestInputStream digestInputStream) {
        final byte[] bytes = digestInputStream.getMessageDigest().digest();
        return Base64.getEncoder().encodeToString(bytes);
    }


    private void closeContentInputStream(InputStream contentInputStream) {
        try {
            contentInputStream.close();
        } catch (IOException e) {
            log.debug("Unable to close content input stream.", e);
        }
    }
//    public String putImage(MultipartFile file) {
//
//
//        Path ROOT = Paths.get("files");
//
//        try {
//            Files.createDirectory(ROOT);
//            Files.copy(file.getInputStream(), ROOT.resolve(file.getOriginalFilename()));
//        } catch (Exception e) {
//            throw new RuntimeException("Couldnot upload");
//        }
//
//
//        return "uploaded";
//    }
}

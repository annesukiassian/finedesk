package tk.finedesk.finedesk.aws.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.sqs.SqsClient;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Value("${aws.region}")
    private String region;

    //    @Value("${aws.access_key_id}")
//    private String access_key_id = "AKIAX4GN3YA5PZEDFBSO";


    //    @Value("${aws.secret.access_key}")
//    private String secret_access_key = "a7payQqXHjq3Pe5GgyruJlst6jb1";


    AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(
            "XXJBHXXXXX",
            "XXJBHXXXXX/");


    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .region(getRegion())
                .build();
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .region(getRegion())
                .build();
    }

    private Region getRegion() {
        return Region.of(region);
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .region(getRegion())
                .build();
    }

}

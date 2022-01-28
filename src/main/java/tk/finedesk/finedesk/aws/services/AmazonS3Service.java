package tk.finedesk.finedesk.aws.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class AmazonS3Service {


    public String putImage(MultipartFile file) {

        Path ROOT = Paths.get("files");

        try {
            Files.createDirectory(ROOT);
            Files.copy(file.getInputStream(), ROOT.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Couldnot upload");
        }


        return "uploaded";
    }
}

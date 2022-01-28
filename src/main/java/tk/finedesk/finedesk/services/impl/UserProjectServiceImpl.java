package tk.finedesk.finedesk.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nonapi.io.github.classgraph.utils.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.finedesk.finedesk.aws.services.AmazonS3Service;
import tk.finedesk.finedesk.dto.response.ResponseProjectDto;
import tk.finedesk.finedesk.repositories.UserProjectRepository;
import tk.finedesk.finedesk.services.ProjectItemService;
import tk.finedesk.finedesk.services.UserProjectService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProjectServiceImpl implements UserProjectService {

    private final UserProjectRepository userProjectRepository;
    private final ProjectItemService projectItemService;
    private AmazonS3Service amazonS3Service;

    @Override
    public ResponseProjectDto addNewItemToProject(List<MultipartFile> images) {

        images.forEach(image -> {
            try {
                File destinationImage = new File("src/main/resources/files/" + image.getOriginalFilename());
                Files.write(destinationImage.toPath(), image.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //TODO create update method and use it.
        return ResponseProjectDto.builder().message("uploaded").build();
    }
}

package tk.finedesk.finedesk.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.finedesk.finedesk.aws.services.AmazonS3Service;
import tk.finedesk.finedesk.dto.response.ResponseProjectDto;
import tk.finedesk.finedesk.entities.UserProfile;
import tk.finedesk.finedesk.entities.UserProject;
import tk.finedesk.finedesk.repositories.UserProjectRepository;
import tk.finedesk.finedesk.services.ProjectItemService;
import tk.finedesk.finedesk.services.UserProfileService;
import tk.finedesk.finedesk.services.UserProjectService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProjectServiceImpl implements UserProjectService {

    private final UserProjectRepository userProjectRepository;
    private final UserProfileService userProfileService;
    private final ProjectItemService projectItemService;
    private AmazonS3Service amazonS3Service;


    @Override
    public ResponseProjectDto addNewItemToProject(List<MultipartFile> images, String username, String projectName, String description) {

        if ((userProjectRepository.findUserProjectByName(projectName)).isPresent()) {
            return ResponseProjectDto.builder().message("Project with that name already exists").build();
        }

        UserProfile profile = userProfileService.findByUsername(username);

        if (profile == null) {
            return ResponseProjectDto.builder().message("The user isn't registered").build();
        }

        images.forEach(image -> {
            try {
                File destinationImage = new File("src/main/resources/files/" + image.getOriginalFilename());
                Files.write(destinationImage.toPath(), image.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        String imageUrl = "/IdeaProjects/finedesk/src/main/resources/files/";

        UserProject userProject = UserProject.builder().creationDate(Date.from(Instant.now())).userProfile(profile).description(description).name(projectName).build();

        UserProject savedProject = userProjectRepository.save(userProject);
        Long projectId = savedProject.getId();

        Set<String> itemUrls = new LinkedHashSet<>();
        images.forEach(
                each -> itemUrls.add(imageUrl + each.getOriginalFilename())
        );

        for (String itemUrl : itemUrls) {
            projectItemService.createProjectItem(itemUrl,projectId);
        }

        //TODO create update method and use it.
        return ResponseProjectDto.builder().message("uploaded").build();
    }
}

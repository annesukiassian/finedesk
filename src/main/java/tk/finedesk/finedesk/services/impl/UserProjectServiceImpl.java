package tk.finedesk.finedesk.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.finedesk.finedesk.aws.services.AmazonS3Service;
import tk.finedesk.finedesk.dto.response.ResponseLikeDto;
import tk.finedesk.finedesk.dto.response.ResponseProjectDto;
import tk.finedesk.finedesk.entities.Like;
import tk.finedesk.finedesk.entities.UserProfile;
import tk.finedesk.finedesk.entities.UserProject;
import tk.finedesk.finedesk.repositories.UserProjectRepository;
import tk.finedesk.finedesk.services.LikeService;
import tk.finedesk.finedesk.services.ProjectItemService;
import tk.finedesk.finedesk.services.UserProfileService;
import tk.finedesk.finedesk.services.UserProjectService;
import tk.finedesk.finedesk.services.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProjectServiceImpl implements UserProjectService {

    private final UserProjectRepository userProjectRepository;
    private final UserService userService;
    private final UserProfileService userProfileService;
    private final ProjectItemService projectItemService;
    private final LikeService likeService;
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
            projectItemService.createProjectItem(itemUrl, projectId);
        }

        //TODO create update method and use it.
        return ResponseProjectDto.builder().message("uploaded").build();
    }

    @Transactional
    @Override
    public ResponseLikeDto likeProject(Long projectId, String username) {

        Optional<UserProject> projectById = userProjectRepository.findById(projectId);

        boolean registeredAsUser = userService.isRegisteredAsUser(username);

        if (registeredAsUser) {

            UserProfile userProfile = userProfileService.findByUsername(username);

            Set<Like> profileLikes = userProfile.getLikes();

            if (projectById.isPresent()) {


                UserProject userProject = projectById.get();

                if (userProject.getUserProfile().getUser().getUsername().equals(username)) {
                    return ResponseLikeDto.builder().message("You can't like your own project").build();
                }

                UserProfile byProjectId = userProfileService.findByProjectId(userProject.getId());

                if (byProjectId != null) {

                    Like likeByProject = likeService.findLikeByProject(userProject);

                    Set<Like> likes = userProject.getLikes();
                    Set<Like> likes1 = userProfile.getLikes();
                    likes1.remove(likeByProject);
                    likes.remove(likeByProject);

                    userProject.setLikes(likes);
                    userProfile.setLikes(likes1);

                    userProfileService.save(userProfile);
                    userProjectRepository.save(userProject);

                    likeService.removeLikeByUserProject(userProject);

                    return ResponseLikeDto.builder().message("You disliked this project").build();
                }

                Like like = Like.builder()
                        .likeDate(Date.from(Instant.now()))
                        .userProject(userProject)
                        .build();

                Like savedLike = likeService.save(like);

                Set<Like> projectLikes = userProject.getLikes();

                projectLikes.add(savedLike);
                profileLikes.add(savedLike);
                userProject.setLikes(projectLikes);
                userProfile.setLikes(profileLikes);

                userProfileService.save(userProfile);
                userProjectRepository.save(userProject);
            }

            return ResponseLikeDto.builder().message("Project liked").build();
        }

        //TODO ete ka, jnjum enq, ete che, avelacnum

        return ResponseLikeDto.builder().message("You can't like this project as Admin").build();

    }
}
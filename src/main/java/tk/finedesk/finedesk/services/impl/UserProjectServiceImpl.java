package tk.finedesk.finedesk.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.finedesk.finedesk.aws.services.AmazonS3Service;
import tk.finedesk.finedesk.dto.response.ResponseLikeDto;
import tk.finedesk.finedesk.dto.response.ResponseProjectDto;
import tk.finedesk.finedesk.dto.response.ResponseProjectItemDto;
import tk.finedesk.finedesk.dto.response.ResponseProjectsDto;
import tk.finedesk.finedesk.entities.Like;
import tk.finedesk.finedesk.entities.ProjectItem;
import tk.finedesk.finedesk.entities.UserProfile;
import tk.finedesk.finedesk.entities.UserProject;
import tk.finedesk.finedesk.repositories.ProjectItemRepository;
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
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserProjectServiceImpl implements UserProjectService {

    private final UserProjectRepository userProjectRepository;
    private final UserService userService;
    private final UserProfileService userProfileService;
    private final ProjectItemService projectItemService;
    private final LikeService likeService;
    private final ProjectItemRepository projectItemRepository;
    private final ModelMapper mapper;
    private AmazonS3Service amazonS3Service;

    @Override
    public ResponseProjectDto createProjectAndUploadImages(List<MultipartFile> images,
                                                           String username, String projectName, String description) {

        UserProfile profile = userProfileService.findByUsername(username);
        if (profile == null) {
            return null;
        }
        writeFiles(images);

        String imageUrl = "/IdeaProjects/finedesk/src/main/resources/files/";
        UserProject savedProject = createUserProject(projectName, description, profile);
        Long projectId = savedProject.getId();
        Set<String> itemUrls = new LinkedHashSet<>();
        images.forEach(
                each -> itemUrls.add(imageUrl + each.getOriginalFilename())
        );
        List<ProjectItem> projectItems = new LinkedList<>();
        for (String itemUrl : itemUrls) {
            ProjectItem projectItem = projectItemService.createProjectItem(itemUrl, projectId);
            projectItems.add(projectItem);
        }
        List<ResponseProjectItemDto> projectItemDtos = projectItems.stream()
                .map(projectItem -> ResponseProjectItemDto.builder()
                        .imageUrl(projectItem.getImageURL())
                        .id(projectItem.getUuid())
                        .build())
                .collect(Collectors.toList());

        return ResponseProjectDto.builder()
                .id(savedProject.getUuid())
                .name(savedProject.getName())
                .description(savedProject.getDescription())
                .creationDate(savedProject.getCreationDate())
                .likeCount((long) savedProject.getLikes().size())
                .projectItems(projectItemDtos)
                .build();
        //TODO create update method and use it.
    }

    private void writeFiles(List<MultipartFile> images) {
        images.forEach(image -> {
            try {
                File destinationImage = new File("src/main/resources/files/" + image.getOriginalFilename());
                Files.write(destinationImage.toPath(), image.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public UserProject createUserProject(String projectName, String description, UserProfile profile) {

        UserProject userProject = new UserProject();
        userProject.setUserProfile(profile);
        userProject.setDescription(description);
        userProject.setCreationDate(Date.from(Instant.now()));
        userProject.setName(projectName);

        return userProjectRepository.save(userProject);
    }

    @Transactional
    @Override
    public ResponseLikeDto likeProject(String projectId, String username) {

//        Optional<UserProject> projectById = userProjectRepository.findById(projectId);

        Optional<UserProject> projectById = userProjectRepository.findByUuid(projectId);

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

    @Override
    public List<ResponseProjectDto> getAllProjects(Integer pageNo, Integer pageSize, Date sortByDate) {

        Pageable creationDate = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "creationDate"));

        Slice<UserProject> all = userProjectRepository.findAll(creationDate);
        if (all.hasContent()) {
            return all.stream()
                    .map(project -> ResponseProjectDto.builder()
                            .description(project.getDescription())
                            .creationDate(project.getCreationDate())
                            .id(project.getUuid())
                            .name(project.getName())
                            .likeCount((long) project.getLikes().size())
                            .build()
                    )
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    @Override
    public boolean ifProjectExists(String projectName, String username) {

        Optional<UserProject> userProjectByName = userProjectRepository.findUserProjectsByUserProfile(projectName, username);

        if (userProjectByName.isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public List<ResponseProjectsDto> getProjectsByProfileUuid(String uuid) {

        List<UserProject> projects = userProjectRepository.findAllByProfileId(uuid);
        return projects.stream()
                .map(userProject ->
                        ResponseProjectsDto.builder()
                                .id(userProject.getUuid())
                                .name(userProject.getName())
                                .creationDate(userProject.getCreationDate())
                                .likeCount((long) userProject.getLikes().size())
                                .build()
                ).collect(Collectors.toList());
    }
}
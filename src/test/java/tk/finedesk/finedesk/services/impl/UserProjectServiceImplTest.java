//package tk.finedesk.finedesk.services.impl;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.modelmapper.ModelMapper;
//import tk.finedesk.finedesk.aws.services.AmazonS3Service;
//import tk.finedesk.finedesk.dto.response.ResponseMessage;
//import tk.finedesk.finedesk.entities.User;
//import tk.finedesk.finedesk.entities.UserProfile;
//import tk.finedesk.finedesk.entities.UserProject;
//import tk.finedesk.finedesk.repositories.UserProjectRepository;
//import tk.finedesk.finedesk.services.LikeService;
//import tk.finedesk.finedesk.services.ProjectItemService;
//import tk.finedesk.finedesk.services.UserProfileService;
//import tk.finedesk.finedesk.services.UserService;
//
//import java.sql.Date;
//import java.time.Instant;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class UserProjectServiceImplTest {
//
//
//    @Mock
//    UserProjectRepository userProjectRepository;
//    @Mock
//    UserService userService;
//    @Mock
//    UserProfileService userProfileService;
//    @Mock
//    ProjectItemService projectItemService;
//    @Mock
//    LikeService likeService;
//    @Mock
//    ModelMapper mapper;
//    @Mock
//    AmazonS3Service amazonS3Service;
//
//    @InjectMocks
//    UserProjectServiceImpl userProjectService;
//
//    private UserProfile profile;
//    private UserProject project;
//
//    @BeforeEach
//    void setUp() {
//        profile = createUserProfile();
//
//        project = createProject();
//    }
//
//    private UserProject createProject() {
//        return UserProject
//                .builder()
//                .id(111L)
//                .name("project1")
//                .description("awesome project")
//                .creationDate(Date.from(Instant.now()))
//                .userProfile(profile)
//                .build();
//    }
//
//    private UserProfile createUserProfile() {
//        return UserProfile
//                .builder()
//                .user(User
//                        .builder()
//                        .username("hg.hvghs@mail.com")
//                        .build())
//                .build();
//    }
//
//    @Test
//    void addNewItemToProject() {
//
//        when(userProjectRepository.findUserProjectByName(any())).thenReturn(Optional.empty());
//        when(userProfileService.findByUsername(profile.getUser().getUsername())).thenReturn(profile);
//        when(userProjectService.createUserProject(project.getName(), project.getDescription(), profile)).thenReturn(project);
//
//        ResponseMessage responseMessage = userProjectService.createProjectAndUploadImages(List.of(), profile.getUser().getUsername(), project.getName(), project.getDescription());
//
//        ResponseMessage expected = ResponseMessage
//                .builder().message("Created new UserProject and uploaded the images").build();
////        var result = userProjectService.addNewItemToProject();
//
//        Assertions.assertEquals(expected, responseMessage);
//    }
//
//    @Test
//    void addNewItemToProject_FailedWhenThereIsProjectExists() {
//
//    }
//
//    @Test
//    void likeProject() {
//    }
//
//    @Test
//    void getAllProjects() {
//    }
//}
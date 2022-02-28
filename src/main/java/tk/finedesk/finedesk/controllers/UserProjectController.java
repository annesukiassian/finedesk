package tk.finedesk.finedesk.controllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tk.finedesk.finedesk.dto.response.ResponseBaseDto;
import tk.finedesk.finedesk.dto.response.ResponseLikeDto;
import tk.finedesk.finedesk.dto.response.ResponseMessage;
import tk.finedesk.finedesk.dto.response.ResponseProjectDto;
import tk.finedesk.finedesk.dto.response.ResponseProjectsDto;
import tk.finedesk.finedesk.services.UserProfileService;
import tk.finedesk.finedesk.services.UserProjectService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Getter
@Setter
@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class UserProjectController {

    private final UserProjectService userProjectService;
    private final UserProfileService userProfileService;

    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(
            value = "/{projectName}",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseBaseDto> createProject(@AuthenticationPrincipal User user, @PathVariable("projectName") String projectName,
                                                         @RequestParam List<MultipartFile> images, @RequestPart("description") String description) {

        boolean exists = userProjectService.ifProjectExists(projectName, user.getUsername());

        if (exists) {
            return ResponseEntity.badRequest().body(ResponseBaseDto.builder().body(ResponseMessage.builder().message("The user already has a project with that name").build()).build());
        }
        try {
            String username = user.getUsername();

            ResponseProjectDto projectAndUploadImages = userProjectService.createProjectAndUploadImages(images, username, projectName, description);

            if (projectAndUploadImages == null) {
                return ResponseEntity.badRequest().body(ResponseBaseDto.builder()
                        .body(ResponseMessage.builder()
                                .message("User isn't registered")
                                .build()).build());
            }

            return ResponseEntity.ok().body(ResponseBaseDto.builder().body(projectAndUploadImages).message("Created project").build());

        } catch (Exception e) {
            log.error("Unable to create project. REASON: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ResponseBaseDto.builder()
                            .body(ResponseMessage.builder()
                                    .message("Something went wrong")
                                    .build())
                            .build());
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @Transactional
    @RequestMapping(value = "/{projectId}/like",
            method = RequestMethod.POST
    )
    public ResponseEntity<ResponseBaseDto> likeProject(@PathVariable("projectId") String projectId, @AuthenticationPrincipal User user) {
        String username = user.getUsername();
        ResponseLikeDto responseLikeDto = userProjectService.likeProject(projectId, username);
        return ResponseEntity
                .ok(ResponseBaseDto.builder().body(responseLikeDto).message("Liked")
                        .build());
    }

    @RequestMapping(
            method = RequestMethod.GET
    )
    public ResponseEntity<List<ResponseBaseDto>> getAllProjectsByCreationDateDesc(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "sortByDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date sortByDate) {

        List<ResponseProjectDto> allProjects = userProjectService.getAllProjects(pageNo, pageSize, sortByDate);

        List<ResponseBaseDto> response = allProjects.stream()
                .map(project -> ResponseBaseDto.builder()
                        .message("OK")
                        .body(project)
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


    @RequestMapping(value = "/{profileId}",
            method = RequestMethod.GET
    )
    public ResponseEntity<List<ResponseBaseDto>> getProjectsByProfileUuid(@AuthenticationPrincipal User user,
                                                                          @PathVariable("profileId") String profileId) {
        try {
            List<ResponseProjectsDto> projectsByProfileUuid = userProjectService.getProjectsByProfileUuid(profileId);
            List<ResponseBaseDto> response = projectsByProfileUuid.stream()
                    .map(project -> ResponseBaseDto.builder()
                            .message("Project of profile { " + profileId + " }")
                            .body(project)
                            .build()
                    ).collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(List.of(ResponseBaseDto.builder().message(e.getMessage()).build()));
        }
    }


}

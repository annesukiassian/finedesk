package tk.finedesk.finedesk.controllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tk.finedesk.finedesk.dto.response.ResponseBaseDto;
import tk.finedesk.finedesk.dto.response.ResponseLikeDto;
import tk.finedesk.finedesk.dto.response.ResponseProjectDto;
import tk.finedesk.finedesk.services.UserProjectService;

import java.util.List;

@Slf4j
@Getter
@Setter
@PreAuthorize("hasAuthority('ROLE_USER')")
@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class UserProjectController {

    private final UserProjectService userProjectService;

    @RequestMapping(
            value = "/{projectName}",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseBaseDto> createProject(@AuthenticationPrincipal User user, @PathVariable String projectName,
                                                         @RequestParam List<MultipartFile> images, @RequestPart("description") String description) {

        String username = user.getUsername();
        log.info("UserName is {} ", username);
        log.info("ProjectName is {} ", projectName);
        log.info("Images is {} ", images);

        ResponseProjectDto responseProjectDto = userProjectService.addNewItemToProject(images, username, projectName, description);

        return ResponseEntity.ok(ResponseBaseDto.builder().body(responseProjectDto).build());
    }

    @RequestMapping(value = "/{projectId}/like",
            method = RequestMethod.POST
    )
    public ResponseEntity<ResponseBaseDto> likeProject(@PathVariable("projectId") Long projectId, @AuthenticationPrincipal User user) {


        String username = user.getUsername();

        ResponseLikeDto responseLikeDto = userProjectService.likeProject(projectId, username);

//        TODO profile service, project service

        return ResponseEntity.ok(ResponseBaseDto.builder().body(responseLikeDto).build());
    }

    @RequestMapping(
            method = RequestMethod.GET
    )
    public ResponseEntity<List<ResponseBaseDto>> getAllProjects() {
        try {
            return ResponseEntity.ok(List.of());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(List.of(ResponseBaseDto.builder().message(e.getMessage()).build()));
        }
    }

}

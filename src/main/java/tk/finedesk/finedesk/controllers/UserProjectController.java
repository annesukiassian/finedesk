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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tk.finedesk.finedesk.dto.response.ResponseBaseDto;
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
    @PostMapping(
            value = "/{projectName}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseBaseDto> createProject(@AuthenticationPrincipal User user, @PathVariable String projectName,
                                                         @RequestParam List<MultipartFile> images) {

        String username = user.getUsername();
        log.info("UserName is {} ", username);
        log.info("ProjectName is {} ", projectName);
        log.info("Images is {} ", images);

        ResponseProjectDto responseProjectDto = userProjectService.addNewItemToProject(images);

        return ResponseEntity.ok(ResponseBaseDto.builder().body(responseProjectDto).build());
    }

    @PostMapping(value = "/{projectId}/like")
    public ResponseEntity<ResponseBaseDto> likeProject(@PathVariable("projectId") long projectId, @AuthenticationPrincipal User user) {

//        TODO profile service, project service

        return ResponseEntity.ok(ResponseBaseDto.builder().build());
    }

    @GetMapping
    public ResponseEntity<List<ResponseBaseDto>> getAllProjects() {
        try {
            return ResponseEntity.ok(List.of());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(List.of(ResponseBaseDto.builder().message(e.getMessage()).build()));
        }
    }

}

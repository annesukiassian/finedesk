package tk.finedesk.finedesk.controllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.finedesk.finedesk.dto.request.RequestImageDto;
import tk.finedesk.finedesk.dto.response.ResponseBaseDto;
import tk.finedesk.finedesk.dto.response.ResponseProjectDto;
import tk.finedesk.finedesk.services.UserProjectService;

import java.util.List;

@Getter
@Setter
//@PreAuthorize("hasRole('ROLE_USER')")
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class UserProjectController {

    private final UserProjectService userProjectService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBaseDto> createProject(@RequestParam("image") RequestImageDto requestImageDto) {

        ResponseProjectDto responseProjectDto = userProjectService.addNewItemToProject(requestImageDto);


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

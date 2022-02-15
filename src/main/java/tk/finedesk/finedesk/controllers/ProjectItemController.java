package tk.finedesk.finedesk.controllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tk.finedesk.finedesk.dto.response.ResponseBaseDto;
import tk.finedesk.finedesk.dto.response.ResponseProjectItemDto;
import tk.finedesk.finedesk.services.ProjectItemService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
@PreAuthorize("hasAuthority('USER')")
@RestController
@RequestMapping("/projectItems")
@RequiredArgsConstructor
public class ProjectItemController {

    private final ProjectItemService projectItemService;

    @RequestMapping(
            method = RequestMethod.GET
    )
    public ResponseEntity<List<ResponseBaseDto>> getAllProjectItems(@AuthenticationPrincipal User user) {
        try {
            return ResponseEntity.ok(List.of());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(List.of(ResponseBaseDto.builder().message(e.getMessage()).build()));
        }
    }

    @RequestMapping(value = "/{projectId}",
            method = RequestMethod.GET
    )
    public ResponseEntity<List<ResponseBaseDto>> getProjectItemsByProjectId(@AuthenticationPrincipal User user,
                                                                            @PathVariable String projectId) {

        try {
            List<ResponseProjectItemDto> projectItemsByProjectId = projectItemService.getProjectItemsByProjectId(projectId);
            List<ResponseBaseDto> dtos = projectItemsByProjectId.stream()
                    .map(item -> ResponseBaseDto.builder()
                            .message("OK")
                            .body(item)
                            .build())
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(List.of(ResponseBaseDto.builder().build()));
        }
    }
}


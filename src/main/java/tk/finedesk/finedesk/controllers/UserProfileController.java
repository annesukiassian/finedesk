package tk.finedesk.finedesk.controllers;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tk.finedesk.finedesk.dto.response.ResponseBaseDto;
import tk.finedesk.finedesk.services.UserProfileService;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@RestController
@RequestMapping("/profiles")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllUserProfiles() {
        try {
            return ResponseEntity.ok(List.of());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(List.of(ResponseBaseDto.builder().message(e.getMessage()).build()));
        }
    }
}

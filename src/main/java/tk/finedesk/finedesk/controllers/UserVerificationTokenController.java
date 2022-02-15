package tk.finedesk.finedesk.controllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tk.finedesk.finedesk.dto.response.ResponseBaseDto;
import tk.finedesk.finedesk.dto.response.ResponseConfirmationDto;
import tk.finedesk.finedesk.services.UserProfileService;
import tk.finedesk.finedesk.services.UserVerificationTokenService;

@Getter
@Setter
@RequiredArgsConstructor
@RestController
@RequestMapping("/verification")
public class UserVerificationTokenController {

    private final UserVerificationTokenService userVerificationTokenService;
    private final UserProfileService userProfileService;

    @RequestMapping(value = "/confirm/{token}",
            method = RequestMethod.GET)
    public ResponseEntity<ResponseBaseDto> confirmRegistration(@PathVariable("token") String token) {

        try {
            ResponseConfirmationDto confirmationDto = userVerificationTokenService.validate(token);

            if (!confirmationDto.isConfirmed()) {
                return ResponseEntity.badRequest().body(ResponseBaseDto.builder()
                        .message("")
                        .body(confirmationDto)
                        .build());
            } else {
                return ResponseEntity.ok(ResponseBaseDto.builder()
                        .message("")
                        .body(confirmationDto)
                        .build());
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ResponseBaseDto.builder()
                            .message(e.getMessage())
                            .build());
        }

    }


}

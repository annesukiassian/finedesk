package tk.finedesk.finedesk.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tk.finedesk.finedesk.dto.request.RequestRegistrationDTO;
import tk.finedesk.finedesk.dto.response.ResponseBaseDto;
import tk.finedesk.finedesk.services.UserService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @RequestMapping(value = "/register",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBaseDto> registerAdmin(@RequestBody @Valid RequestRegistrationDTO adminDto) {
        try {
            boolean userExists = userService.isUserExists(adminDto);
            if (!userExists) {
                ResponseBaseDto responseBaseDto = userService.registerAdmin(adminDto);
                return ResponseEntity.ok(responseBaseDto);
            } else {
                userService.addAdminRoleToExistingUser(adminDto);
                return ResponseEntity.ok(ResponseBaseDto.builder().message("Added Admin Role").build());
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ResponseBaseDto.builder().message(e.getMessage())
                            .build());
        }
    }

}

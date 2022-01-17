package tk.finedesk.finedesk.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.finedesk.finedesk.dto.request.RequestRegistrationDTO;
import tk.finedesk.finedesk.dto.response.ResponseBaseDto;
import tk.finedesk.finedesk.services.UserService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController("/user")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseBaseDto> registerUser(@RequestBody @Valid RequestRegistrationDTO userDto) {
        try {
            boolean userExists = userService.isUserExists(userDto);
            if (!userExists) {
                ResponseBaseDto responseBaseDto = userService.registerUser(userDto);
                return ResponseEntity.ok(responseBaseDto);
            } else {
                return ResponseEntity.ok(ResponseBaseDto.builder().message("Check your email").build());
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseBaseDto.builder().message(e.getMessage()).build());
        }
    }

}

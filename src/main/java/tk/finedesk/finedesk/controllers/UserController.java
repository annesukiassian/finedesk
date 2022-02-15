package tk.finedesk.finedesk.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @RequestMapping(value = "/register",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
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
            return ResponseEntity.internalServerError()
                    .body(ResponseBaseDto.builder().message(e.getMessage())
                            .build());
        }
    }


    @RequestMapping(value = "/{userId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBaseDto> getUserById(@PathVariable String userId) {
        if (userId != null) {
            ResponseBaseDto userById = userService.getUserById(userId);
            return ResponseEntity.ok()
                    .body(userById);
        }
        return ResponseEntity
                .internalServerError()
                .body(ResponseBaseDto.builder().build());
    }

}

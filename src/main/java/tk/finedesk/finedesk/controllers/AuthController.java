package tk.finedesk.finedesk.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tk.finedesk.finedesk.dto.response.ResponseBaseDto;
import tk.finedesk.finedesk.security.dto.RequestAuthenticationDto;
import tk.finedesk.finedesk.services.UserService;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @RequestMapping(value = "/login",
            method = RequestMethod.POST)
    public ResponseEntity<ResponseBaseDto> login(@RequestBody @Valid RequestAuthenticationDto requestAuthenticationDto) {

        try {
            String username = requestAuthenticationDto.getUsername();
            String password = requestAuthenticationDto.getPassword();

            log.info("username " + username + " password: " + password);

            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            } catch (BadCredentialsException e) {
                throw new BadCredentialsException("Bad credentials");
            }

            ResponseBaseDto responseBaseDto = userService.checkUser(username);

            return ResponseEntity
                    .ok()
                    .body(responseBaseDto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseBaseDto.builder().message(e.getMessage()).build());
        }
    }

    @RequestMapping(value = "/access_denied",
            method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseBaseDto> accessDenied() {
        return ResponseEntity.status(403).body(ResponseBaseDto.builder().message("Access denied").build());
    }


}

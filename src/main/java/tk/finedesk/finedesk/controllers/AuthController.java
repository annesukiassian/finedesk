package tk.finedesk.finedesk.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.finedesk.finedesk.dto.response.ResponseBaseDto;
import tk.finedesk.finedesk.security.dto.RequestAuthenticationDto;
import tk.finedesk.finedesk.services.UserService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<ResponseBaseDto> login(@RequestBody @Valid RequestAuthenticationDto requestAuthenticationDto) {

        try {

            String username = requestAuthenticationDto.getUsername();
            String password = requestAuthenticationDto.getPassword();

            try {
                Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            } catch (BadCredentialsException e) {
                throw new BadCredentialsException("Bad credentials");
            }

            ResponseBaseDto responseBaseDto = userService.checkUser(username);

            return ResponseEntity.ok(responseBaseDto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseBaseDto.builder().message(e.getMessage()).build());
        }


    }

    @GetMapping("/access_denied")
    public ResponseEntity<ResponseBaseDto> accessDenied() {
        return ResponseEntity.status(403).body(ResponseBaseDto.builder().message("Access denied").build());
    }
}

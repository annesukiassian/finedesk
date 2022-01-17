package tk.finedesk.finedesk.controllers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.finedesk.finedesk.dto.response.ResponseBaseDto;
import tk.finedesk.finedesk.dto.response.ResponseLoginDto;
import tk.finedesk.finedesk.security.dto.RequestAuthenticationDto;
import tk.finedesk.finedesk.security.jwt.JwtCreator;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class AuthController {

    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtCreator jwtCreator;

    @PostMapping("/login")
    public ResponseEntity<ResponseBaseDto> login(@RequestBody @Valid RequestAuthenticationDto requestAuthenticationDto) {

        try {

            String username = requestAuthenticationDto.getUsername();
            String password = requestAuthenticationDto.getPassword();

            if (username == null || password == null) {
                throw new BadCredentialsException("You didn't provide username or password");
            }
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            } catch (BadCredentialsException e) {
                throw new BadCredentialsException("Bad credentials");
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            List<SimpleGrantedAuthority> roles = userDetails.getAuthorities().stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());

            final String jwt = jwtCreator.generateToken(username, Pair.of("roles", roles.toString()));

            return ResponseEntity.ok(ResponseBaseDto.builder().body(ResponseLoginDto.builder().jwt(jwt).build()).build());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseBaseDto.builder().message(e.getMessage()).build());
        }


    }

    @GetMapping("/access_denied")
    public ResponseEntity<ResponseBaseDto> accessDenied() {
        return ResponseEntity.status(403).body(ResponseBaseDto.builder().message("Access denied").build());
    }
}

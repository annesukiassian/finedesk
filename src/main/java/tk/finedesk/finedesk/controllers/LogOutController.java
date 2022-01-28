package tk.finedesk.finedesk.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.finedesk.finedesk.dto.response.ResponseBaseDto;
import tk.finedesk.finedesk.repositories.UserRepository;
import tk.finedesk.finedesk.repositories.UserVerificationTokenRepository;
import tk.finedesk.finedesk.security.CustomLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class LogOutController {

    private final UserRepository userRepository;
    private final UserVerificationTokenRepository userVerificationTokenRepository;


    @PostMapping(value = "/logout")
    public ResponseEntity<ResponseBaseDto> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            try {
                new CustomLogoutSuccessHandler(userRepository, userVerificationTokenRepository).onLogoutSuccess(request, response, authentication);
            } catch (IOException | ServletException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok(ResponseBaseDto.builder().message("successfully logged out").build());
    }
}

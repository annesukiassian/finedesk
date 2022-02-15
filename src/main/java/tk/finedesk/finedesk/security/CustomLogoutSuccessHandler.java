package tk.finedesk.finedesk.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import tk.finedesk.finedesk.entities.User;
import tk.finedesk.finedesk.entities.UserVerificationToken;
import tk.finedesk.finedesk.enums.TokenType;
import tk.finedesk.finedesk.repositories.UserRepository;
import tk.finedesk.finedesk.repositories.UserVerificationTokenRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final UserRepository userRepository;
    private final UserVerificationTokenRepository userVerificationTokenRepository;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication != null && authentication.getDetails() != null) {
            try {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                User user = userRepository.findByUsername(auth.getName());
                user.setActive(false);
                UserVerificationToken userVerificationToken = user.getUserVerificationToken();
                TokenType type = userVerificationToken.getType();
                if (type == TokenType.REFRESH) {
                    user.setUserVerificationToken(null);
                    userVerificationTokenRepository.delete(userVerificationToken);
                }
                request.getSession().invalidate();
                log.info("User successfully logged out");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect("/auth/login");
    }
}

package tk.finedesk.finedesk.security;


import com.amazonaws.services.ssooidc.model.ExpiredTokenException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.Pair;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import tk.finedesk.finedesk.entities.UserVerificationToken;
import tk.finedesk.finedesk.enums.TokenType;
import tk.finedesk.finedesk.repositories.UserRepository;
import tk.finedesk.finedesk.security.jwt.JwtCreator;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final JwtCreator jwtCreator;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwt = extractJwtFromRequest(request);
        try {
            if (jwt != null) {
                if (validateToken(jwt)) {
                    UserDetails userDetails = new User(getUsernameFromToken(jwt), "", getRolesFromToken(jwt));
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } else {
                    //TODO move this part into cache
                    String refreshTokenUuidFromAccessToken = getRefreshTokenFromAccessToken(jwt);
                    if (refreshTokenUuidFromAccessToken != null) {
                        Date now = Date.from(Instant.from(ZonedDateTime.now()));
                        String userName = JWT.decode(jwt).getSubject();
                        tk.finedesk.finedesk.entities.User user = userRepository.findByUsername(userName);
                        String uuid1 = user.getUuid();
                        UserVerificationToken userVerificationToken = user.getUserVerificationToken();
                        List<SimpleGrantedAuthority> rolesFromToken = getRolesFromToken(jwt);
                        List<String> rolesString = rolesFromToken.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());
                        String uuidString = refreshTokenUuidFromAccessToken.replaceAll("\"", "");
                        if (userVerificationToken.getType().equals(TokenType.REFRESH) && (userVerificationToken.getUuid()).equalsIgnoreCase(uuidString)) {
                            if (userVerificationToken.getExpirationTime().before(now)) {
                                Date expiresAt = JWT.decode(jwt).getExpiresAt();
                                log.info("Old expiration time {} ", expiresAt);
                                String accessToken = jwtCreator.createAccessToken(userName, Pair.of("refreshTokenUuid", uuidString),
                                        ChronoUnit.MINUTES, Pair.of("userUuid", uuid1), Pair.of("userRoles", rolesString));
                                response.addHeader("Authorization", "Bearer " + accessToken);
                            }
                        }
                    }
                }

            }
        } finally {
            filterChain.doFilter(request, response);
        }

    }

    private String getRefreshTokenFromAccessToken(String jwt) {
        Claim refreshToken = JWT.decode(jwt).getClaim("refreshTokenUuid");
        return refreshToken.toString();
    }

    private List<SimpleGrantedAuthority> getRolesFromToken(String jwt) {
        List<Claim> userRoles1 = List.of(JWT.decode(jwt).getClaim("userRoles"));
        Claim a = JWT.decode(jwt).getClaim("userRoles");
        List<String> roles = new JwtCreator().getRolesFromString(String.valueOf(a));
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    private String getUsernameFromToken(String jwt) {
        return JWT.decode(jwt).getSubject();
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean validateToken(String jwt) {
        try {

            Date expiresAt = JWT.decode(jwt).getExpiresAt();

            if (Date.from(Instant.now()).after(expiresAt)) {
                return false;
            }
            return true;
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new BadCredentialsException("Invalid credentials", e);
        } catch (ExpiredTokenException ex) {
            throw ex;
        }
    }
}


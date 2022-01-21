package tk.finedesk.finedesk.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import tk.finedesk.finedesk.repositories.UserRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String jwt = extractJwtFromRequest(request);


        String usernameFromToken = getUsernameFromToken(jwt);

        List<SimpleGrantedAuthority> rolesFromToken = getRolesFromToken(jwt);

        if (jwt != null) {


            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(usernameFromToken, null, rolesFromToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
            //   UserDetails userDetails = new User(usernameFromToken, "", rolesFromToken);
//            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        }

    }

    private List<SimpleGrantedAuthority> getRolesFromToken(String jwt) {
//        Claim userRoles = JWT.decode(jwt).getClaim("userRoles");
//        List<SimpleGrantedAuthority> collect = userRoles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        List<Claim> userRoles1 = List.of(JWT.decode(jwt).getClaim("userRoles"));
        var a =  JWT.decode(jwt).getClaim("userRoles");
        System.out.println(JWT.decode(jwt).getClaim("userRoles"));
        System.out.println(userRoles1);
        return null;
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


}

package tk.finedesk.finedesk.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.modelmapper.internal.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtCreator {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String username, Pair<String, String> claims) throws IllegalAccessException {


        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        var issuedAt = Instant.now();
        var expiredAt = issuedAt.plus(1, ChronoUnit.DAYS);

        if (claims == null) {
            throw new IllegalArgumentException("Arguments are invalid");
        }

        return JWT.create()
                .withSubject(username)
                .withClaim(claims.getLeft(), claims.getRight())
                .withIssuedAt(Date.from(issuedAt))
                .withExpiresAt(Date.from(expiredAt))
                .sign(algorithm);
    }

}


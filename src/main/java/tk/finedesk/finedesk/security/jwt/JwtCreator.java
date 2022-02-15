package tk.finedesk.finedesk.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.Pair;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtCreator {

    public String generateVerificationToken(String username, Pair<String, String> verificationUuidPair, ChronoUnit hours) {

        Algorithm algorithm = Algorithm.HMAC256("secret");
        Instant issuedAt = Instant.now();
        Instant expiredAt = issuedAt.plus(1, hours);

        return JWT.create()
                .withSubject(username)
                .withClaim(verificationUuidPair.getLeft(), verificationUuidPair.getRight())
                .withIssuedAt(Date.from(issuedAt))
                .withExpiresAt(Date.from(expiredAt))
                .sign(algorithm);

    }

//    public String generateRefreshToken(String username, ChronoUnit days) {
//        Algorithm algorithm = Algorithm.HMAC256("mysupersecret");
//
//        Instant issuedAt = Instant.now();
//        Instant expiredAt = issuedAt.plus(7, days);
//
//        return JWT.create()
//                .withSubject(username)
//                .withIssuedAt(Date.from(issuedAt))
//                .withExpiresAt(Date.from(expiredAt))
//                .sign(algorithm);
//
//    }

    public String createAccessToken(String username, Pair<String, String> refreshTokenUuid, ChronoUnit minutes, Pair<String, String> userUuid, Pair<String, List<String>> userRoles) {

        Algorithm algorithm = Algorithm.HMAC256("mysupersecret");
        Instant issuedAt = Instant.now();
        Instant expiredAt = issuedAt.plus(15, minutes);

        log.info("Creating Access token");

        return JWT.create()
                .withSubject(username)
                .withClaim(refreshTokenUuid.getLeft(), refreshTokenUuid.getRight())
                .withClaim(userUuid.getLeft(), userUuid.getRight())
                .withClaim(userRoles.getLeft(), getRolesStringify(userRoles.getRight()))
                .withIssuedAt(Date.from(issuedAt))
                .withExpiresAt(Date.from(expiredAt))
                .sign(algorithm);
    }

    private String getRolesStringify(List<String> rolesList) {
        if (null == rolesList || rolesList.isEmpty()) {
            return "";
        }
        if (rolesList.size() == 1) {
            return rolesList.get(0);
        }
        return String.join(",", rolesList);
    }

    public List<String> getRolesFromString(final String roleString) {
        if (roleString != null || roleString != "") {
            String[] result = roleString.replaceAll("\"", "").split(", ");
            return Arrays.asList(result);
        }
        return null;
    }
}


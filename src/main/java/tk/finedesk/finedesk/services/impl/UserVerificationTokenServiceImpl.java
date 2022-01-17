package tk.finedesk.finedesk.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import lombok.Setter;
import tk.finedesk.finedesk.entities.User;
import tk.finedesk.finedesk.entities.UserVerificationToken;

import java.util.Date;

@Getter
@Setter
public class TokenGenerator {


    public UserVerificationToken generateToken(User savedUser) {

        UserVerificationToken userVerificationToken = new UserVerificationToken();

        Algorithm algorithm = Algorithm.HMAC256("secretaf".getBytes());

        String verification_token = JWT.create()
                .withSubject(savedUser.getUsername())
                .withClaim("uuid", savedUser.getUUID())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .sign(algorithm);

        userVerificationToken.setVerificationToken(verification_token);

        return userVerificationToken;
    }


}

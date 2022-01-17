package tk.finedesk.finedesk.services.impl;

import com.auth0.jwt.JWT;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.Pair;
import org.springframework.stereotype.Service;
import tk.finedesk.finedesk.dto.response.ResponseConfirmationDto;
import tk.finedesk.finedesk.entities.UserVerificationToken;
import tk.finedesk.finedesk.repositories.UserVerificationTokenRepository;
import tk.finedesk.finedesk.security.jwt.JwtCreatorForVerification;
import tk.finedesk.finedesk.services.UserVerificationTokenService;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
@Service
public class UserVerificationTokenServiceImpl implements UserVerificationTokenService {

    private final JwtCreatorForVerification jwtCreator;

    private final UserVerificationTokenRepository userVerificationTokenRepository;


    @Override
    public UserVerificationToken saveToken(String username) throws IllegalAccessException {

        UserVerificationToken userVerificationToken = new UserVerificationToken();

        //send this token by SQS

        String jwtToken = jwtCreator.generateToken(username, Pair.of("uuid", userVerificationToken.getUuid()));

        log.info("jwtToken is {}", jwtToken);

        UserVerificationToken savedToken = userVerificationTokenRepository.save(userVerificationToken);

        return savedToken;
    }

    @Override
    public ResponseConfirmationDto validate(String token) throws Exception {
        try {

            var response = ResponseConfirmationDto.builder().build();

            if (token != null) {


                String uuidFromToken = JWT.decode(token).getClaim("uuid").asString();
                String subjectFromToken = JWT.decode(token).getSubject();

                log.info("uuid {}", uuidFromToken);
                log.info("subject {}", subjectFromToken);

                if (uuidFromToken != null && subjectFromToken != null) {

                    Optional<UserVerificationToken> byUuid = userVerificationTokenRepository.getByUuid(uuidFromToken);

                    if (byUuid.isPresent()) {
                        Date expirationTime = byUuid.get().getExpirationTime();
                        Date now = Date.from(Instant.from(ZonedDateTime.now()));
                        if (now.before(expirationTime)) {
                            response.setConfirmed(true);
                        }
                    }
                }

            }

            return response;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

//    @Override
//    public String generateToken(String username, Collection<SimpleGrantedAuthority> roles) throws IllegalAccessException {
//        return generateToken(username, roles, null);
//    }

//    public String generateToken(String username, Collection<SimpleGrantedAuthority> roles, UserVerificationToken userVerificationToken) throws IllegalAccessException {
//
//        Algorithm algorithm = Algorithm.HMAC256("secretaf".getBytes());
//        var issuedAt = Instant.now();
//        var expiredAt = issuedAt.plus(1, ChronoUnit.DAYS);
//        Pair<String, String> claims = null;
//
//
//        if (roles == null) {
//
//            claims = Pair.of("uuid", userVerificationToken.getUuid());
//            expiredAt = issuedAt.plus(1, ChronoUnit.HOURS);
//
//        } else if (userVerificationToken == null) {
//
//            claims = Pair.of("roles", roles.toString());
//
//        }else {
//            throw new IllegalAccessException("Arguments are invalid");
//        }
//
//        return JWT.create()
//                .withSubject(username)
//                .withClaim(claims.getLeft(), claims.getRight())
//                .withIssuedAt(Date.from(issuedAt))
//                .withExpiresAt(Date.from(expiredAt))
//                .sign(algorithm);
//    }

}

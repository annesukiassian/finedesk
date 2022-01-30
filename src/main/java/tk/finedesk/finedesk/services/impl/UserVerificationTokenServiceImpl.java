package tk.finedesk.finedesk.services.impl;

import com.auth0.jwt.JWT;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.Pair;
import org.springframework.stereotype.Service;
import tk.finedesk.finedesk.dto.response.ResponseConfirmationDto;
import tk.finedesk.finedesk.entities.User;
import tk.finedesk.finedesk.entities.UserVerificationToken;
import tk.finedesk.finedesk.enums.TokenType;
import tk.finedesk.finedesk.repositories.UserRepository;
import tk.finedesk.finedesk.repositories.UserVerificationTokenRepository;
import tk.finedesk.finedesk.security.jwt.JwtCreator;
import tk.finedesk.finedesk.services.UserProfileService;
import tk.finedesk.finedesk.services.UserVerificationTokenService;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
@Service
public class UserVerificationTokenServiceImpl implements UserVerificationTokenService {

    private final JwtCreator jwtCreator;
    private final UserVerificationTokenRepository userVerificationTokenRepository;
    private final UserRepository userRepository;
    private final UserProfileService userProfileService;

    @Override
    public UserVerificationToken generateVerificationToken(String username) throws IllegalArgumentException {

        UserVerificationToken userVerificationToken = new UserVerificationToken();
        userVerificationToken.setType(TokenType.VERIFICATION);

        Pair<String, String> verificationUuidPair = Pair.of("uuid", userVerificationToken.getUuid());

        ChronoUnit hours = ChronoUnit.HOURS;

        String verificationToken = jwtCreator.generateVerificationToken(username, verificationUuidPair, hours);

        //TODO send this token by SQS

        log.info("jwtToken is {}", verificationToken);

        return userVerificationTokenRepository.save(userVerificationToken);
    }

    @Override
    public ResponseConfirmationDto validate(String token) throws Exception {
        try {

            ResponseConfirmationDto response = ResponseConfirmationDto.builder().build();

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
                            UserVerificationToken userVerificationToken = byUuid.get();
                            if (userVerificationToken.getType().equals(TokenType.VERIFICATION)) {
                                User byVerificationToken = userRepository.findByVerificationToken(userVerificationToken);
                                byVerificationToken.setActive(true);
                                userProfileService.createProfile(byVerificationToken);
                                userRepository.saveAndFlush(byVerificationToken);
                                byVerificationToken.setUserVerificationToken(null);
                                userVerificationTokenRepository.delete(userVerificationToken);
                                response.setConfirmed(true);
                            }

                        }
                    }
                }
            }
            return response;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public UserVerificationToken generateRefreshToken(String username) {

        User userByUsername = userRepository.findByUsername(username);

        log.info("Generating refresh token");

        UserVerificationToken userRefreshToken = new UserVerificationToken();

        userRefreshToken.setType(TokenType.REFRESH);

        ChronoUnit days = ChronoUnit.DAYS;

        userByUsername.setUserVerificationToken(userRefreshToken);
        log.info("Refresh Token set in User table");

        return userVerificationTokenRepository.save(userRefreshToken);
    }
}

package tk.finedesk.finedesk.security.jwt;

import lombok.RequiredArgsConstructor;
import tk.finedesk.finedesk.entities.UserVerificationToken;
import tk.finedesk.finedesk.enums.TokenType;
import tk.finedesk.finedesk.repositories.UserVerificationTokenRepository;

import java.util.Optional;

//@RequiredArgsConstructor
//public class JwtUtil {
//
//    private final UserVerificationTokenRepository userVerificationTokenRepository;
//
//    public Optional<UserVerificationToken> getByUuidAndType(String tokenUuid, TokenType refresh) {
//        return userVerificationTokenRepository.getByUuidAndType(tokenUuid, refresh);
//    }
//}

package tk.finedesk.finedesk.services;

import tk.finedesk.finedesk.dto.response.ResponseConfirmationDto;
import tk.finedesk.finedesk.entities.UserVerificationToken;

public interface UserVerificationTokenService {

    UserVerificationToken generateVerificationToken(String username) throws IllegalArgumentException;

    ResponseConfirmationDto validate(String token) throws Exception;

    UserVerificationToken generateRefreshToken(String username);


//    String generateToken(String username, Collection<SimpleGrantedAuthority> roles) throws IllegalAccessException;
}

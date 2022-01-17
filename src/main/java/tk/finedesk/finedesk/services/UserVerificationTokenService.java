package tk.finedesk.finedesk.services;

import tk.finedesk.finedesk.dto.response.ResponseConfirmationDto;
import tk.finedesk.finedesk.entities.UserVerificationToken;

public interface UserVerificationTokenService {

    UserVerificationToken saveToken(String username) throws IllegalAccessException;

    ResponseConfirmationDto validate(String token) throws Exception;

//    String generateToken(String username, Collection<SimpleGrantedAuthority> roles) throws IllegalAccessException;
}

package tk.finedesk.finedesk.services;

import tk.finedesk.finedesk.dto.request.RequestRegistrationDTO;
import tk.finedesk.finedesk.dto.response.ResponseBaseDto;
import tk.finedesk.finedesk.entities.User;


public interface UserService {

    ResponseBaseDto registerUser(RequestRegistrationDTO userDto) throws IllegalAccessException;

    User getUser(String username);

    boolean isUserExists(RequestRegistrationDTO userDto);


}

package tk.finedesk.finedesk.services;

import tk.finedesk.finedesk.dto.request.RequestRegistrationDTO;
import tk.finedesk.finedesk.dto.response.ResponseBaseDto;
import tk.finedesk.finedesk.entities.User;

import javax.management.relation.RoleNotFoundException;


public interface UserService {

    ResponseBaseDto registerUser(RequestRegistrationDTO userDto) throws IllegalAccessException, RoleNotFoundException;

    User getUserByUsername(String username);

    boolean isUserExists(RequestRegistrationDTO userDto);


    ResponseBaseDto checkUser(String username) throws IllegalAccessException;
}

package tk.finedesk.finedesk.services;

import tk.finedesk.finedesk.dto.request.RequestRegistrationDTO;
import tk.finedesk.finedesk.dto.response.ResponseBaseDto;
import tk.finedesk.finedesk.entities.User;

import javax.management.relation.RoleNotFoundException;
import java.util.List;


public interface UserService {

    ResponseBaseDto registerUser(RequestRegistrationDTO userDto) throws IllegalAccessException, RoleNotFoundException;

    ResponseBaseDto registerAdmin(RequestRegistrationDTO adminDto) throws RoleNotFoundException;

    User getUserByUsername(String username);

    boolean isUserExists(RequestRegistrationDTO userDto) throws RoleNotFoundException;


    ResponseBaseDto checkUser(String username) throws IllegalAccessException;


    ResponseBaseDto getUserById(String userId);

    void addAdminRoleToExistingUser(RequestRegistrationDTO adminDto) throws RoleNotFoundException;

    boolean isRegisteredAsUser(String username);
}

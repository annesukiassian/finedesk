package tk.finedesk.finedesk.services;


import tk.finedesk.finedesk.entities.User;
import tk.finedesk.finedesk.entities.UserProfile;

public interface UserProfileService {

    void createProfile(User user);

    UserProfile findByUsername(String username);
}

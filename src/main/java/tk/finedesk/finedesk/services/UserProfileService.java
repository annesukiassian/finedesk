package tk.finedesk.finedesk.services;


import tk.finedesk.finedesk.entities.User;
import tk.finedesk.finedesk.entities.UserProfile;

public interface UserProfileService {

    void createProfile(User user);

    UserProfile save(UserProfile userProfile);

    UserProfile findByUsername(String username);

    UserProfile findByProjectId(Long projectId);
}

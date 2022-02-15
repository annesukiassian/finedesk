package tk.finedesk.finedesk.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.finedesk.finedesk.entities.User;
import tk.finedesk.finedesk.entities.UserProfile;
import tk.finedesk.finedesk.repositories.UserProfileRepository;
import tk.finedesk.finedesk.services.UserProfileService;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Override
    public void createProfile(User user) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        user.setProfile(userProfile);
    }

    @Override
    public UserProfile save(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }




    @Override
    public UserProfile findByUsername(String username) {

        Optional<UserProfile> profile = userProfileRepository.findByUsername(username);
        if (profile.isPresent()) {
            return profile.get();
        }
        return null;
    }

    @Override
    public UserProfile findByProjectId(Long projectId) {
        return userProfileRepository.findProfileByProjectLikes(projectId);
    }
}

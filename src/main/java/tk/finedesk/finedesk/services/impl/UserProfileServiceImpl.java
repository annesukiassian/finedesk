package tk.finedesk.finedesk.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.finedesk.finedesk.dto.response.ResponseProfileDto;
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
    public UserProfile createProfile(User user) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        user.setProfile(userProfile);
        return userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfile save(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfile findByUsername(String username) {
        Optional<UserProfile> profile = userProfileRepository.findByUsername(username);
        return profile.orElse(null);
    }

    @Override
    public UserProfile findByProjectId(Long projectId) {
        return userProfileRepository.findProfileByProjectLikes(projectId);
    }

    @Override
    public ResponseProfileDto uploadProfilePhoto(MultipartFile profilePhoto) {

        //TODO call S3 service to upload photos

        return null;
    }
}

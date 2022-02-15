package tk.finedesk.finedesk.services;


import org.springframework.web.multipart.MultipartFile;
import tk.finedesk.finedesk.dto.response.ResponseProfileDto;
import tk.finedesk.finedesk.entities.User;
import tk.finedesk.finedesk.entities.UserProfile;

public interface UserProfileService {

    UserProfile createProfile(User user);

    UserProfile save(UserProfile userProfile);

    UserProfile findByUsername(String username);

    UserProfile findByProjectId(Long projectId);

    ResponseProfileDto uploadProfilePhoto(MultipartFile profilePhoto);
}

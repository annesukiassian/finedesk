package tk.finedesk.finedesk.services;

import tk.finedesk.finedesk.entities.Like;
import tk.finedesk.finedesk.entities.UserProfile;
import tk.finedesk.finedesk.entities.UserProject;

import java.util.List;

public interface LikeService {

//    Like createPostLike(UserProfile byUsername, UserProject userProject);

    List<Like> getLikestByUserProject(UserProject userProject);
//
//    List<Like> getLikesByProfiles(List<UserProfile> userProfile);

    Like save(Like like);
}

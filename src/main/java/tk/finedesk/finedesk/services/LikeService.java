package tk.finedesk.finedesk.services;

import org.springframework.transaction.annotation.Transactional;
import tk.finedesk.finedesk.entities.Like;
import tk.finedesk.finedesk.entities.UserProject;

public interface LikeService {

//    Like createPostLike(UserProfile byUsername, UserProject userProject);

//    List<Like> getLikestByUserProject(UserProject userProject);
//
//    List<Like> getLikesByProfiles(List<UserProfile> userProfile);

    @Transactional
    void removeLikeByUserProject(UserProject userProject);

    Like save(Like like);

    Like findLikeByProject(UserProject userProject);
}

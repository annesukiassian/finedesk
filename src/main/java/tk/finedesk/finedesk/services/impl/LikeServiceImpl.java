package tk.finedesk.finedesk.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.finedesk.finedesk.entities.Like;
import tk.finedesk.finedesk.entities.UserProfile;
import tk.finedesk.finedesk.entities.UserProject;
import tk.finedesk.finedesk.repositories.LikeRepository;
import tk.finedesk.finedesk.services.LikeService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;


//    @Override
//    public Like createPostLike(UserProfile userProfile, UserProject userProject) {
//
//
////        Post like = Post.builder()
////                .postDate(Date.from(Instant.now()))
////                .postType(PostType.LIKE)
////                .userProfiles(userProfiles)
////                .userProjects(userProjects)
////                .build();
//
//        return null;
//    }

    @Override
    public List<Like> getLikestByUserProject(UserProject userProject) {
        return likeRepository.findByUserProject(userProject);
    }

//    @Override
//    public List<Like> getLikesByProfiles(List<UserProfile> userProfile) {
//        return likeRepository.findByUserProfiles(userProfile);
//    }

    @Override
    public Like save(Like like) {
        return likeRepository.save(like);
    }
}

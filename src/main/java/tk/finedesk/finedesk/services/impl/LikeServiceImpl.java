package tk.finedesk.finedesk.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.finedesk.finedesk.entities.Like;
import tk.finedesk.finedesk.entities.UserProject;
import tk.finedesk.finedesk.repositories.LikeRepository;
import tk.finedesk.finedesk.services.LikeService;

@RequiredArgsConstructor
@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    @Transactional
    @Override
    public void removeLikeByUserProject(UserProject userProject) {
        likeRepository.removeLikeByUserProject(userProject);
    }

    @Override
    public Like save(Like like) {
        return likeRepository.save(like);
    }

    @Override
    public Like findLikeByProject(UserProject userProject) {
        return likeRepository.findLikeByUserProject(userProject);
    }


}

package tk.finedesk.finedesk.services;

import org.springframework.transaction.annotation.Transactional;
import tk.finedesk.finedesk.entities.Like;
import tk.finedesk.finedesk.entities.UserProject;

public interface LikeService {

    @Transactional
    void removeLikeByUserProject(UserProject userProject);

    Like save(Like like);

    Like findLikeByProject(UserProject userProject);
}

package tk.finedesk.finedesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tk.finedesk.finedesk.entities.Like;
import tk.finedesk.finedesk.entities.UserProject;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    @Transactional
    void removeLikeByUserProject(UserProject userProject);

    Like findLikeByUserProject(UserProject userProject);
}

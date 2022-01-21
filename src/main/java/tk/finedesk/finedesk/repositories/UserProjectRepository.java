package tk.finedesk.finedesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tk.finedesk.finedesk.entities.UserProject;

@Repository
public interface UserProjectRepository extends JpaRepository<UserProject, Long>, PagingAndSortingRepository<UserProject, Long> {

    UserProject findUserProjectByName(String name);
}

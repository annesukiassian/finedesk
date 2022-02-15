package tk.finedesk.finedesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tk.finedesk.finedesk.entities.UserSkills;

@Repository
public interface UserSkillsRepository extends JpaRepository<UserSkills,Long> {
}

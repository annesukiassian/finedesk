package tk.finedesk.finedesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tk.finedesk.finedesk.entities.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository <UserProfile,Long>{
}

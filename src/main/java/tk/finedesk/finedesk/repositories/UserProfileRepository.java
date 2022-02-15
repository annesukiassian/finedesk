package tk.finedesk.finedesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tk.finedesk.finedesk.entities.UserProfile;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    @Query(value = "SELECT u FROM UserProfile u WHERE u.user.username=:username")
    Optional<UserProfile> findByUsername(@Param("username") String username);

    @Query(value = "SELECT DISTINCT u FROM UserProfile u JOIN u.likes l WHERE l.userProject.id=:id")
    UserProfile findProfileByProjectLikes(@Param("id") Long id);


}

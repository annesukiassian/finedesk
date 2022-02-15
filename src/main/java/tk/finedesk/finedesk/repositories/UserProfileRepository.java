package tk.finedesk.finedesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tk.finedesk.finedesk.entities.UserProfile;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {


    @Query(value = "select u from UserProfile u where u.user.username=:username")
    Optional<UserProfile> findByUsername(@Param("username") String username);

    @Query(value = "select u from UserProfile u where u.user.uuid=:uuid")
    Optional<UserProfile> findByUserUuid(@Param("uuid") String uuid);

    @Query(value = "select distinct u from UserProfile u join u.likes l where l.userProject.id=:id")
    UserProfile findProfileByProjectLikes(@Param("id") Long id);


}

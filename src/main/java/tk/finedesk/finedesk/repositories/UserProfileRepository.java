package tk.finedesk.finedesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.config.web.servlet.oauth2.resourceserver.OAuth2ResourceServerSecurityMarker;
import org.springframework.stereotype.Repository;
import tk.finedesk.finedesk.entities.UserProfile;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {


    @Query("select u from UserProfile u where u.user.username=:username")
    Optional<UserProfile> findByUsername(@Param("username") String username);

    @Query("select u from UserProfile u where u.user.uuid=:uuid")
    Optional<UserProfile> findByUserUuid(@Param("uuid") String uuid);
}

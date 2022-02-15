package tk.finedesk.finedesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tk.finedesk.finedesk.entities.UserProject;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProjectRepository extends JpaRepository<UserProject, Long>, PagingAndSortingRepository<UserProject, Long> {

    @Query(value = "SELECT p FROM UserProject p WHERE p.uuid=:uuid")
    Optional<UserProject> findByUuid(@Param("uuid") String uuid);

    @Query(value = "SELECT u FROM UserProject u WHERE u.name=:name AND u.userProfile.user.username=:username")
    Optional<UserProject> findUserProjectsByUserProfile(@Param("name") String name, @Param("username") String username);

    @Query(value = "SELECT u FROM UserProject u WHERE u.userProfile.uuid=:uuid ORDER BY u.creationDate DESC")
    List<UserProject> findAllByProfileId(@Param("uuid") String uuid);

}

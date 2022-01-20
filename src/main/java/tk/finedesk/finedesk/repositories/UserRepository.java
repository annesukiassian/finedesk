package tk.finedesk.finedesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tk.finedesk.finedesk.entities.User;
import tk.finedesk.finedesk.entities.UserVerificationToken;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    @Query("Select u from User u WHERE u.username=:username")
    User findByUsername(@Param("username") String username);

    @Query("Select u from UserRole u WHERE u.role=:role")
    User findByRole(@Param("role") String role);


    @Query("Select u from User u WHERE u.uuid=:userUuid")
    User findByUuid(@Param("userUuid") String userUuid);

    @Query("select u from User u WHERE u.userVerificationToken=:token")
    User findByVerificationToken(@Param("token") UserVerificationToken token);
}

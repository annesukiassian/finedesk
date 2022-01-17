package tk.finedesk.finedesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Jpa21Utils;
import org.springframework.stereotype.Repository;
import tk.finedesk.finedesk.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
}

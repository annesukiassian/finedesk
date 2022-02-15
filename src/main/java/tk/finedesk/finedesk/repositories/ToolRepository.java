package tk.finedesk.finedesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tk.finedesk.finedesk.entities.Tool;

@Repository
public interface ToolRepository extends JpaRepository<Tool,Long> {
}

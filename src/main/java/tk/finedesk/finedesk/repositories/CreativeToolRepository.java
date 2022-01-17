package tk.finedesk.finedesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tk.finedesk.finedesk.entities.CreativeTool;

@Repository
public interface CreativeToolRepository extends JpaRepository<CreativeTool,Long> {
}

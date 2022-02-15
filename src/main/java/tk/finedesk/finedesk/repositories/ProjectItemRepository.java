package tk.finedesk.finedesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tk.finedesk.finedesk.entities.ProjectItem;

import java.util.List;

@Repository
public interface ProjectItemRepository extends JpaRepository<ProjectItem, Long> {

    @Query(value = "SELECT i FROM ProjectItem i WHERE i.userProject.uuid=:uuid")
    List<ProjectItem> findAllByProjectId(@Param("uuid") String projectId);

}

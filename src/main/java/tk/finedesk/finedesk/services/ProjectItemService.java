package tk.finedesk.finedesk.services;

import org.springframework.web.multipart.MultipartFile;
import tk.finedesk.finedesk.entities.ProjectItem;

public interface ProjectItemService {

    ProjectItem createProjectItem(MultipartFile image);
}

package tk.finedesk.finedesk.services;

import tk.finedesk.finedesk.entities.ProjectItem;

public interface ProjectItemService {

    ProjectItem createProjectItem(String imageUrl, Long projectId);
}

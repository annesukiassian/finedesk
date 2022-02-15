package tk.finedesk.finedesk.services;

import tk.finedesk.finedesk.dto.response.ResponseProjectItemDto;
import tk.finedesk.finedesk.entities.ProjectItem;

import java.util.List;

public interface ProjectItemService {

    ProjectItem createProjectItem(String imageUrl, Long projectId);

    List<ResponseProjectItemDto> getProjectItemsByProjectId(String projectId);
}

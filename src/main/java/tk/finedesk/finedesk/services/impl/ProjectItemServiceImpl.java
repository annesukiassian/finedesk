package tk.finedesk.finedesk.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.finedesk.finedesk.dto.response.ResponseProjectItemDto;
import tk.finedesk.finedesk.entities.ProjectItem;
import tk.finedesk.finedesk.repositories.ProjectItemRepository;
import tk.finedesk.finedesk.repositories.UserProjectRepository;
import tk.finedesk.finedesk.services.ProjectItemService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectItemServiceImpl implements ProjectItemService {

    private final ProjectItemRepository projectItemRepository;
    private final UserProjectRepository userProjectRepository;

    @Override
    public ProjectItem createProjectItem(String imageUrl, Long projectId) {

        ProjectItem projectItem = new ProjectItem();
        projectItem.setImageURL(imageUrl);
        projectItem.setUserProject(userProjectRepository.getById(projectId));

        //TODO upload to S3
        //TODO create ProjectItem
        //TODO SAVE to DB

        return projectItemRepository.save(projectItem);
    }

    @Override
    public List<ResponseProjectItemDto> getProjectItemsByProjectId(String projectId) {

        List<ProjectItem> allByProjectId = projectItemRepository.findAllByProjectId(projectId);

        return allByProjectId.stream()
                .map(projectItem -> ResponseProjectItemDto.builder()
                        .id(projectItem.getUuid())
                        .imageUrl(projectItem.getImageURL())
                        .build())
                .collect(Collectors.toList());
    }
}

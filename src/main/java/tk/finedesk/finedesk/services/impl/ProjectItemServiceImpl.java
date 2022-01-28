package tk.finedesk.finedesk.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.finedesk.finedesk.entities.ProjectItem;
import tk.finedesk.finedesk.repositories.ProjectItemRepository;
import tk.finedesk.finedesk.repositories.UserProjectRepository;
import tk.finedesk.finedesk.services.ProjectItemService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectItemServiceImpl implements ProjectItemService {

    private final ProjectItemRepository projectItemRepository;
    private final UserProjectRepository userProjectRepository;

    @Override
    public ProjectItem createProjectItem(String imageUrl, Long projectId) {

        //  ProjectItem projectItem = ProjectItem.builder().imageURL(imageUrl).build();

        ProjectItem projectItem1 = new ProjectItem();
        projectItem1.setImageURL(imageUrl);
        projectItem1.setUserProject(userProjectRepository.getById(projectId));

        //TODO upload to S3
        //TODO create ProjectItem
        //TODO SAVE to DB

        return projectItemRepository.save(projectItem1);
    }
}

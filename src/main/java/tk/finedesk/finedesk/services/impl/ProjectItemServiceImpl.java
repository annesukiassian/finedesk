package tk.finedesk.finedesk.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.finedesk.finedesk.entities.ProjectItem;
import tk.finedesk.finedesk.repositories.ProjectItemRepository;
import tk.finedesk.finedesk.services.ProjectItemService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectItemServiceImpl implements ProjectItemService {

    private final ProjectItemRepository projectItemRepository;

    @Override
    public ProjectItem createProjectItem(MultipartFile image) {




        //TODO upload to S3
        //TODO create ProjectItem
        //TODO SAVE to DB

        return null;
    }
}

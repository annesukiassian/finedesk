package tk.finedesk.finedesk.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.finedesk.finedesk.repositories.ProjectItemRepository;
import tk.finedesk.finedesk.services.ProjectItemService;


@Service
@RequiredArgsConstructor
public class ProjectItemServiceImpl implements ProjectItemService {

    private final ProjectItemRepository projectItemRepository;

}

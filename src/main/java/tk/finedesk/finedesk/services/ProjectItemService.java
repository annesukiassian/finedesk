package tk.finedesk.finedesk.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.finedesk.finedesk.repositories.ProjectItemRepository;

@Service
@RequiredArgsConstructor
public class ProjectItemService {

    private final ProjectItemRepository projectItemRepository;

}

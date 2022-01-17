package tk.finedesk.finedesk.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.finedesk.finedesk.repositories.UserProjectRepository;
import tk.finedesk.finedesk.services.UserProfileService;
import tk.finedesk.finedesk.services.UserProjectService;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProjectRepository userProjectRepository;

}

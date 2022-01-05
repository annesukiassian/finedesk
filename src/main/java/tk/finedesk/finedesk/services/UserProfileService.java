package tk.finedesk.finedesk.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.finedesk.finedesk.repositories.UserProjectRepository;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProjectRepository userProjectRepository;

}

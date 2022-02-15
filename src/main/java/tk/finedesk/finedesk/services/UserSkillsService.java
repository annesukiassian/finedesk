package tk.finedesk.finedesk.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.finedesk.finedesk.repositories.UserSkillsRepository;

@Service
@RequiredArgsConstructor
public class UserSkillsService {

    private final UserSkillsRepository userSkillsRepository;

}

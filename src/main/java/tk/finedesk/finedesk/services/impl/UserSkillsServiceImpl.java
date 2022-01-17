package tk.finedesk.finedesk.services.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.finedesk.finedesk.repositories.UserSkillsRepository;
import tk.finedesk.finedesk.services.UserSkillsService;

@Service
@RequiredArgsConstructor
public class UserSkillsServiceImpl implements UserSkillsService {

    private final UserSkillsRepository userSkillsRepository;

}

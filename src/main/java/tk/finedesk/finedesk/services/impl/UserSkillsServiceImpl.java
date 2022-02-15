package tk.finedesk.finedesk.services.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.finedesk.finedesk.repositories.UserSkillsRepository;
import tk.finedesk.finedesk.services.UserSkillsService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSkillsServiceImpl implements UserSkillsService {

    private final UserSkillsRepository userSkillsRepository;

}

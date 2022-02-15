package tk.finedesk.finedesk.controllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.finedesk.finedesk.services.UserSkillsService;

@Getter
@Setter
@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
public class UserSkillsController {

    private final UserSkillsService userSkillsService;

}

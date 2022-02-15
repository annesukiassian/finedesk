package tk.finedesk.finedesk.controllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.finedesk.finedesk.services.UserProjectService;

@Getter
@Setter
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class UserProjectController {

    private final UserProjectService userProjectService;


}

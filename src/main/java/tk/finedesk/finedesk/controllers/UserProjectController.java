package tk.finedesk.finedesk.controllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.finedesk.finedesk.entities.UserProject;
import tk.finedesk.finedesk.services.UserProjectService;

import java.util.List;

@Getter
@Setter
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class UserProjectController {

    private final UserProjectService userProjectService;



}

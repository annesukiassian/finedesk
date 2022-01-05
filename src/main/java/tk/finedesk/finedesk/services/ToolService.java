package tk.finedesk.finedesk.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import tk.finedesk.finedesk.repositories.ToolRepository;

@Service
@RequiredArgsConstructor
public class ToolService {

    private final ToolRepository toolRepository;

}

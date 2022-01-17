package tk.finedesk.finedesk.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.finedesk.finedesk.repositories.CreativeToolRepository;
import tk.finedesk.finedesk.services.CreativeToolService;

@Service
@RequiredArgsConstructor
public class CreativeToolServiceImpl implements CreativeToolService {

    private final CreativeToolRepository creativeToolRepository;

}

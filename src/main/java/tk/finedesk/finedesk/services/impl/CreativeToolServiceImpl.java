package tk.finedesk.finedesk.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tk.finedesk.finedesk.repositories.CreativeToolRepository;
import tk.finedesk.finedesk.services.CreativeToolService;

@Slf4j
@RequiredArgsConstructor
public class CreativeToolServiceImpl implements CreativeToolService {

    private final CreativeToolRepository creativeToolRepository;

}

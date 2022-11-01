package org.rinatzzak.service.impl;

import lombok.extern.log4j.Log4j;
import org.rinatzzak.service.ConsumerService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Log4j
public class ConsumerServiceImpl implements ConsumerService {
    @Override
    public void consumeTextMessageUpdates(Update update) {
        log.debug("NODE: Text message is received");
    }

    @Override
    public void consumeDocumentMessageUpdates(Update update) {
        log.debug("NODE: Document message is received");
    }

    @Override
    public void consumePhotoMessageUpdates(Update update) {
        log.debug("NODE: Document message is received");
    }
}

package org.rinatzzak.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MainService {
    void processMessageText(Update update);
}

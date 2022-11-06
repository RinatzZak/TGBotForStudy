package org.rinatzzak.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MainService {
    void processMessageText(Update update);
    void processDocumentText(Update update);
    void processPhotoText(Update update);
}

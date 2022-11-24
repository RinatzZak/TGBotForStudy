package org.rinatzzak.service;

import org.rinatzzak.entity.enums.AppDocument;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    AppDocument processDoc(Message tgMessage);
}

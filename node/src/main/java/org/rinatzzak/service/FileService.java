package org.rinatzzak.service;

import org.rinatzzak.entity.AppDocument;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    AppDocument processDoc(Message tgMessage);
}

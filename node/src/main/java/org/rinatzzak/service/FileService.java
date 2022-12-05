package org.rinatzzak.service;

import org.rinatzzak.entity.AppDocument;
import org.rinatzzak.entity.AppPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    AppDocument processDoc(Message tgMessage);
    AppPhoto processPhoto(Message tgMessage);
}

package org.rinatzzak.service;

import org.rinatzzak.entity.AppDocument;
import org.rinatzzak.entity.AppPhoto;
import org.rinatzzak.service.enums.LinkType;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    AppDocument processDoc(Message tgMessage);
    AppPhoto processPhoto(Message tgMessage);
    String generateLink(Long docId, LinkType linkType);
}

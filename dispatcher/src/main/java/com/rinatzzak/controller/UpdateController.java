package com.rinatzzak.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Log4j
public class UpdateController {
    private TelegramBot telegramBot;

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("Received update is null!");
        }

        if (update != null && update.getMessage() != null) {
            distributeMessageForType(update);
        } else {
            log.error("Received unsupported message type: " + update);
        }
    }

    private void distributeMessageForType(Update update) {
        var message = update.getMessage();
        if (message.getText() != null) {
            processTextMessage(update);
        } else if (message.getDocument() != null) {
            processDocumentMessage(update);
        } else if (message.getPhoto() != null) {
            processPhotoMessage(update);
        } else {
            setUnsupportedMessageTypeView(update);
        }
    }

    private void setUnsupportedMessageTypeView(Update update) {
    }

    private void processPhotoMessage(Update message) {
    }

    private void processDocumentMessage(Update message) {
    }

    private void processTextMessage(Update message) {
    }
}

package org.rinatzzak.service.impl;

import org.rinatzzak.dao.RawDataDao;
import org.rinatzzak.entity.RawData;
import org.rinatzzak.service.MainService;
import org.rinatzzak.service.ProduceService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class MainServiceImpl implements MainService {
    private final RawDataDao rawDataDao;
    private final ProduceService produceService;

    public MainServiceImpl(RawDataDao rawDataDao, ProduceService produceService) {
        this.rawDataDao = rawDataDao;
        this.produceService = produceService;
    }

    @Override
    public void processMessageText(Update update) {
        saveRawData(update);

        var message = update.getMessage();
        var sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Hello from NODE");
        produceService.produceAnswerMessage(sendMessage);
    }

    public void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();

        rawDataDao.save(rawData);
    }
}

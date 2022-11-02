package org.rinatzzak.service.impl;

import org.junit.jupiter.api.Test;
import org.rinatzzak.dao.RawDataDao;
import org.rinatzzak.entity.RawData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MainServiceImplTest {
    @Autowired
    private RawDataDao rawDataDao;

    @Test
    void saveRawData() {
        Update update = new Update();
        Message message = new Message();
        message.setText("Hello from test!");
        update.setMessage(message);

        RawData rawData = RawData.builder()
                .event(update)
                .build();

        Set<RawData> rawDataSet = new HashSet<>();
        rawDataSet.add(rawData);
        rawDataDao.save(rawData);

        Assert.isTrue(rawDataSet.contains(rawData), "Entity not found in the set!");
    }
}
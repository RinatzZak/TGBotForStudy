package org.rinatzzak.service.impl;

import lombok.extern.log4j.Log4j;
import org.rinatzzak.dao.AppUserDao;
import org.rinatzzak.dao.RawDataDao;
import org.rinatzzak.entity.AppUser;
import org.rinatzzak.entity.RawData;
import org.rinatzzak.service.MainService;
import org.rinatzzak.service.ProduceService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.rinatzzak.entity.enums.UserState.BASIC_STATE;
import static org.rinatzzak.entity.enums.UserState.WAIT_FOR_EMAIL_STATE;
import static org.rinatzzak.service.enums.ServiceCommands.CANCEL;

@Service
@Log4j
public class MainServiceImpl implements MainService {
    private final RawDataDao rawDataDao;
    private final ProduceService produceService;
    private final AppUserDao appUserDao;

    public MainServiceImpl(RawDataDao rawDataDao, ProduceService produceService, AppUserDao appUserDao) {
        this.rawDataDao = rawDataDao;
        this.produceService = produceService;
        this.appUserDao = appUserDao;
    }

    @Override
    public void processMessageText(Update update) {
        saveRawData(update);

        var appUser = findOrSaveAppUser(update);
        var userState = appUser.getState();
        var text = update.getMessage().getText();
        var output = "";

        if (CANCEL.equals(text)) {
            output = cancelProcess(appUser);
        } else if (BASIC_STATE.equals(userState)) {
            output = processServiceCommand(appUser, text);
        } else if (WAIT_FOR_EMAIL_STATE.equals(userState)) {
            //TODO добавить обработку регистрации email
        } else {
            log.error("Unknown user state: " + userState);
            output = "Unknown error! Please enter /cancel and try again!";
        }

        var chatId = update.getMessage().getChatId();
        sendAnswer(output, chatId);
    }

    private void sendAnswer(String output, Long chatId) {
    }

    private String processServiceCommand(AppUser appUser, String text) {
    }

    private String cancelProcess(AppUser appUser) {
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();

        rawDataDao.save(rawData);
    }

    private AppUser findOrSaveAppUser(Update update) {
        AppUser persistentAppUser = appUserDao.findAppUserByTelegramUserId(update.getId());
        if (persistentAppUser == null) {
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .firstname(telegramUser.getFirstName())
                    .lastname(telegramUser.getLastName())
                    .username(telegramUser.getUserName())
                    //TODO изменить значение по умолчанию после регистрации
                    .isActive(true)
                    .state(BASIC_STATE)
                    .build();
            return appUserDao.save(transientAppUser);
        }
        return persistentAppUser;
    }

}

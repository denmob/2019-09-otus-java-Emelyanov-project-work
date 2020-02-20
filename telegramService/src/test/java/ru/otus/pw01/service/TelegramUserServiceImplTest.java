package ru.otus.pw01.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.otus.pw01.TelegramService;
import ru.otus.pw01.controller.RegTelegramApi;
import ru.otus.pw01.model.TelegramUser;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TelegramService.class)
public class TelegramUserServiceImplTest  extends RegTelegramApi {

    @Autowired
    private TelegramUserService telegramUserService;

    @Test
    public void saveUserIfNotExist() {
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setUserId(475757602L);
        telegramUser.setFirstName("testFirstName");
        telegramUser.setPhoneNumber("380937188891");
        telegramUserService.saveUserIfNotExist(telegramUser);
    }
}

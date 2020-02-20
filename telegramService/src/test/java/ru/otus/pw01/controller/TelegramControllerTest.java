package ru.otus.pw01.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.otus.pw01.TelegramController;
import ru.otus.pw01.TelegramService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TelegramService.class)
public class TelegramControllerTest  extends RegTelegramApi{

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(TelegramControllerTest.class);

    @Autowired
    private TelegramController telegramController;

    @Test
    public void sendTestMessage() {
        SendMessage message = new SendMessage("475757602", "test");
        telegramController.sendToUser(message);
    }

}

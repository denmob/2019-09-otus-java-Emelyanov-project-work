package ru.otus.pw01.service;

import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.pw01.controller.RegTelegramApi;
import ru.otus.pw01.model.TelegramUser;

import static junit.framework.TestCase.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class TelegramUserServiceImplTest  extends RegTelegramApi {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    private final  static long DEV_CHAT_ID = 475757602L;
    private final static String DEV_ALLOW_USER_PHONE_NUMBER_SET = "80937188891";

    @Autowired
    private TelegramUserService telegramUserService;

    @Test
    @Order(1)
    public void saveUserIfNotExist() {
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setUserId(DEV_CHAT_ID);
        telegramUser.setFirstName("testFirstName");
        telegramUser.setPhoneNumber(DEV_ALLOW_USER_PHONE_NUMBER_SET);
        telegramUserService.saveUserIfNotExist(telegramUser);
    }

    @Test
    @Order(2)
    public void findUserByPhoneNumberNotNull() {
        TelegramUser telegramUser = telegramUserService.findUserByPhoneNumber(DEV_ALLOW_USER_PHONE_NUMBER_SET);
        assertNotNull(telegramUser);
    }

    @Test
    @Order(3)
    public void findUserByPhoneNumberNull() {
        TelegramUser telegramUser = telegramUserService.findUserByPhoneNumber("xxxxxx");
        assertNull(telegramUser);
    }

    @Test
    @Order(4)
    public void findUserByUserIDNotNull() {
        TelegramUser telegramUser = telegramUserService.findUserByUserID(DEV_CHAT_ID);
        assertNotNull(telegramUser);
    }

    @Test
    @Order(5)
    public void findUserByUserIDNull() {
        TelegramUser telegramUser = telegramUserService.findUserByUserID(123L);
        assertNull(telegramUser);
    }

}
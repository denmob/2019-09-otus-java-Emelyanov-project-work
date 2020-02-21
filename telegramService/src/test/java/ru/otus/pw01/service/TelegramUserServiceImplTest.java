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

    @Autowired
    private TelegramUserService telegramUserService;

    @Test
    @Order(1)
    public void saveUserIfNotExist() {
        logger.debug("order 1");
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setUserId(475757602L);
        telegramUser.setFirstName("testFirstName");
        telegramUser.setPhoneNumber("380937188891");
        telegramUserService.saveUserIfNotExist(telegramUser);
    }

    @Test
    @Order(2)
    public void findUserByPhoneNumberNotNull() {
        logger.debug("order 2");
        TelegramUser telegramUser = telegramUserService.findUserByPhoneNumber("380937188891");
        assertNotNull(telegramUser);
    }

    @Test
    @Order(3)
    public void findUserByPhoneNumberNull() {
        logger.debug("order 3");
        TelegramUser telegramUser = telegramUserService.findUserByPhoneNumber("xxxxxx");
        assertNull(telegramUser);
    }

    @Test
    @Order(4)
    public void findUserByUserIDNotNull() {
        logger.debug("order 4");
        TelegramUser telegramUser = telegramUserService.findUserByUserID(475757602L);
        assertNotNull(telegramUser);
    }

    @Test
    @Order(5)
    public void findUserByUserIDNull() {
        logger.debug("order 5");
        TelegramUser telegramUser = telegramUserService.findUserByUserID(123L);
        assertNull(telegramUser);
    }

}

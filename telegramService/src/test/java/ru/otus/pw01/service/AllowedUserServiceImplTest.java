package ru.otus.pw01.service;

import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.pw01.controller.RegTelegramApi;
import ru.otus.pw01.model.AllowedUser;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AllowedUserServiceImplTest extends RegTelegramApi {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AllowedUserService allowedUserService;

    @Test
    public void findUserByPhoneNumberNotNull() {
        AllowedUser allowedUser = allowedUserService.findUserByPhoneNumber("380937188891");
        assertNotNull(allowedUser);
    }

    @Test
    public void findUserByPhoneNumberNull() {
        AllowedUser allowedUser = allowedUserService.findUserByPhoneNumber("xxxxxx");
        assertNull(allowedUser);
    }
}

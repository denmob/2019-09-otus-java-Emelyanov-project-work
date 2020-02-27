package ru.otus.pw01.service;

import org.junit.Test;
import org.junit.runner.RunWith;
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

    @Autowired
    private AllowedUserService allowedUserService;

    @Test
    public void findAllowedUserByPhoneNumberNotNull() {
        AllowedUser allowedUser = allowedUserService.findAllowedUserByPhoneNumber("380937188891");
        assertNotNull(allowedUser);
    }

    @Test
    public void findAllowedUserByPhoneNumberNull() {
        AllowedUser allowedUser = allowedUserService.findAllowedUserByPhoneNumber("xxxxxx");
        assertNull(allowedUser);
    }
}

package ru.otus.pw01.service;

import ru.otus.pw01.model.AllowedUser;

public interface AllowedUserService {

    /**
     * Searches AllowedUser by  phoneNumber in DB
     *
     * @param phoneNumber - phoneNumber
     * @return - instance of AllowedUser or null
     */
    AllowedUser findAllowedUserByPhoneNumber(String phoneNumber);
}

package ru.otus.pw01.service;


import ru.otus.pw01.model.TelegramUser;

public interface TelegramUserService {

    /**
     * Searches TelegramUser by  telegramUserId in DB
     *
     * @param userID - telegramUserId
     * @return - instance of TelegramUser or null
     */
    TelegramUser findUserByUserID(Long userID);

    /**
     * Searches TelegramUser by  phoneNumber in DB
     *
     * @param phoneNumber - phoneNumber
     * @return - instance of TelegramUser or null
     */
    TelegramUser findUserByPhoneNumber(String phoneNumber);

    /**
     * Saves TelegramUser into DB if there no such user. Check by telegramUserId
     *
     * @param userToSave - user that need to be saved
     */
    void saveUserIfNotExist(TelegramUser userToSave);

}


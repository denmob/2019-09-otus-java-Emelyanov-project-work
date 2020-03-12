package ru.otus.pw01.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.pw01.model.TelegramUser;
import ru.otus.pw01.repository.TelegramUserRepository;


@Service
public class TelegramUserServiceImpl implements TelegramUserService {

    private final TelegramUserRepository telegramUserRepository;

    public TelegramUserServiceImpl(TelegramUserRepository telegramUserRepository) {
        this.telegramUserRepository = telegramUserRepository;
    }

    /**
     * Searches TelegramUser by  telegramUserId in DB
     *
     * @param userID - telegramUserId
     * @return - instance of TelegramUser or null
     */
    @Override
    public TelegramUser findTelegramUserByUserID(Long userID) {
        return telegramUserRepository.findTelegramUserByUserId(userID);
    }

    /**
     * Searches TelegramUser by  phoneNumber in DB
     *
     * @param phoneNumber - phoneNumber
     * @return - instance of TelegramUser or null
     */
    @Override
    public TelegramUser findTelegramUserByPhoneNumber(String phoneNumber) {
        return telegramUserRepository.findTelegramUserByPhoneNumber(phoneNumber);
    }

    /**
     * Saves TelegramUser into DB if there no such user. Check by telegramUserId
     *
     * @param userToSave - user that need to be saved
     */
    @Override
    @Transactional
    public void saveTelegramUserIfNotExist(TelegramUser userToSave) {
        TelegramUser foundUser = findTelegramUserByUserID(userToSave.getUserId());
        if (foundUser == null) {
            telegramUserRepository.save(userToSave);
        }
    }
}

package ru.otus.pw01.service;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.pw01.model.TelegramUser;
import ru.otus.pw01.repository.TelegramUserRepository;

@Service
public class TelegramUserServiceImpl implements TelegramUserService {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(TelegramUserServiceImpl.class);

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
    public TelegramUser findUserByUserID(Long userID) {
        return telegramUserRepository.findTelegramUserByUserId(userID);
    }

    @Override
    public TelegramUser findUserByPhoneNumber(String phoneNumber) {
        return telegramUserRepository.findTelegramUserByPhoneNumber(phoneNumber);
    }

    /**
     * Saves TelegramUser into DB if there no such user. Check by telegramUserId
     *
     * @param userToSave - user that need to be saved
     */
    @Override
    @Transactional
    public void saveUserIfNotExist(TelegramUser userToSave) {
        TelegramUser foundUser = findUserByUserID(userToSave.getUserId());
        if (foundUser == null) {
            telegramUserRepository.save(userToSave);
        }
    }
}

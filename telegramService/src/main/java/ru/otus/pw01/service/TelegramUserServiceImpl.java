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

    @Autowired
    private TelegramUserRepository telegramUserRepository;

    /**
     * Searches TelegramUser by  telegramUserId in DB
     *
     * @param telegramUserId - telegramUserId
     * @return - instance of TelegramUser or null
     */
    @Override
    public TelegramUser findUserByTelegramUserId(Long telegramUserId) {
        return telegramUserRepository.findByTelegramUserId(telegramUserId);
    }

    @Override
    public TelegramUser findUserByPhoneNumber(String phoneNumber) {
        return telegramUserRepository.findByPhoneNumber(phoneNumber);
    }

    /**
     * Saves TelegramUser into DB if there no such user. Check by telegramUserId
     *
     * @param userToSave - user that need to be saved
     */
    @Override
    @Transactional
    public void saveUserIfNotExist(TelegramUser userToSave) {
        logger.debug("saveUserIfNotExist userToSave={}",userToSave);
        TelegramUser foundUser = findUserByTelegramUserId(userToSave.getTelegramUserId());
        if (foundUser == null) {
            logger.debug("User not found. userToSave={} ", userToSave);
            telegramUserRepository.save(userToSave);
        }
    }
}

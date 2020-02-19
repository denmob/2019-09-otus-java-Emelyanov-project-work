package ru.otus.pw01.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.pw01.model.TelegramUser;

@Repository
public interface TelegramUserRepository extends MongoRepository<TelegramUser, String> {
    TelegramUser findByUserID(Long telegramUserId);

    TelegramUser findByPhoneNumber(String phoneNumber);
}

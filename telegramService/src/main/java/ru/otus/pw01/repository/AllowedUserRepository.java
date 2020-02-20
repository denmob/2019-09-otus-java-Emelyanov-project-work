package ru.otus.pw01.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.pw01.model.AllowedUser;

@Repository
public interface AllowedUserRepository extends MongoRepository<AllowedUser, String> {

    AllowedUser findAllowedUserByPhoneNumber(String phoneNumber);

}

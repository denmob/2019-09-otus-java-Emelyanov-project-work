package ru.otus.pw02.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.pw.library.model.UserData;

import java.util.List;

@Repository
public interface UserDataRepository extends MongoRepository<UserData, String> {

    UserData findUserDataByUserID(Long userID);
    List<UserData> findAll();
}

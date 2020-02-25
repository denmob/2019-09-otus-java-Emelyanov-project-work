package ru.otus.pw02.service;

import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import ru.otus.pw.library.model.UserData;
import ru.otus.pw02.repository.UserDataRepository;

import java.util.List;

@Service
public class UserDataServiceImpl implements UserDataService {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDataRepository userDataRepository;

    public UserDataServiceImpl(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    /**
     * Searches UserData by userId in DB
     *
     * @param userID - telegramUserId
     * @return - instance of UserData or null
     */
    @Override
    public UserData findUserDataByUserId(Long userID) {
        return userDataRepository.findUserDataByUserID(userID);
    }

    /**
     * Saves UserData into DB if there no such userData. Check by UserId
     *
     * @param userDataToSave - user that need to be saved
     */
    @Override
    public void saveUserDataIfNotExist(UserData userDataToSave) {
        UserData foundUser = findUserDataByUserId(userDataToSave.getUserID());
        if (foundUser == null) {
            userDataRepository.save(userDataToSave);
        }
    }

    @Override
    public List<UserData> getAllUserData() {
        return userDataRepository.findAll();
    }
}

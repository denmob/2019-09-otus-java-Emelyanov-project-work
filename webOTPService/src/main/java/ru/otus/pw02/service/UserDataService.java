package ru.otus.pw02.service;

import ru.otus.pw.library.model.UserData;

public interface UserDataService {

    /**
     * Searches UserData by userId in DB
     *
     * @param userID - telegramUserId
     * @return - instance of UserData or null
     */
    UserData findUserDataByUserId(Long userID);


    /**
     * Saves UserData into DB if there no such userData. Check by UserId
     *
     * @param userDataToSave - user that need to be saved
     */
    void saveUserDataIfNotExist(UserData userDataToSave);

}


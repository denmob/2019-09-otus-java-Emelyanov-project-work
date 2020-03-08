package ru.otus.pw01.service;

import org.springframework.stereotype.Service;
import ru.otus.pw01.model.AllowedUser;
import ru.otus.pw01.repository.AllowedUserRepository;

@Service
public class AllowedUserServiceImpl implements AllowedUserService{

    private final AllowedUserRepository allowedUserRepository;

    public AllowedUserServiceImpl(AllowedUserRepository allowedUserRepository) {
        this.allowedUserRepository = allowedUserRepository;
    }

    /**
     * Searches AllowedUser by  phoneNumber in DB
     *
     * @param phoneNumber - phoneNumber
     * @return - instance of AllowedUser or null
     */
    @Override
    public AllowedUser findAllowedUserByPhoneNumber(String phoneNumber) {
        return allowedUserRepository.findAllowedUserByPhoneNumber(phoneNumber);
    }
}

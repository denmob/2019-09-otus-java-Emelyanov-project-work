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

    @Override
    public AllowedUser findUserByPhoneNumber(String phoneNumber) {
        return allowedUserRepository.findAllowedUserByPhoneNumber(phoneNumber);
    }
}

package ru.otus.pw02.service;

public interface OtpService {

    long generateOTP(String key);

    long getOtp(String key);
}

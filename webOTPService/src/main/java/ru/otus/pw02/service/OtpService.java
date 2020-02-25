package ru.otus.pw02.service;

public interface OtpService {

    long generateOTP(int hash);

    boolean checkOtp(long otp);
}

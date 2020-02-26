package ru.otus.pw02.service;

import java.util.Map;

public interface OtpService {

    long generateOTP(int hash);
    boolean checkOtp(long otp);
    Map<Integer, Long> getViewActualOTP();
}

package ru.otus.pw02.service;

import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@SpringBootTest
public class OtpServiceImplTest {

    private final Logger logger = LoggerFactory.getLogger(OtpServiceImplTest.class);
    private final OtpService otpService = new OtpServiceImpl(1);

    @Test
    public void generateOTP() {
        String s ="Hello";
        long otp = otpService.generateOTP(s);
        logger.info("generated OTP: {}", otp);
        assertTrue(otp>0);
    }

    @Test
    public void getOtp() {
        String s ="Hello";
        long otp = otpService.generateOTP(s);
        String optString =  String.valueOf(otp);
        otp = Long.parseLong(optString);
        logger.info("generated OTP: {}", otp);
        assertTrue( otpService.checkOtp(otp));
    }

    @Test
    public void getOtpFail() throws InterruptedException {
        String s = "Hello";
        long otp = otpService.generateOTP(s);
        logger.info("generated OTP: {}", otp);
        Thread.sleep(TimeUnit.SECONDS.toMillis(61));
        assertFalse(otpService.checkOtp(otp));
    }

}

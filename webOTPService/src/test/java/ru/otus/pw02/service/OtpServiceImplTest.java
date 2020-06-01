package ru.otus.pw02.service;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

@SpringBootTest
public class OtpServiceImplTest {

    private final Logger logger = LoggerFactory.getLogger(OtpServiceImplTest.class);
    private final OtpService otpService = new OtpServiceImpl(5, TimeUnit.SECONDS);

    @Test
    public void generateOTP() {
        String s = "Hello";
        long otp = otpService.generateOTP(s.hashCode());
        logger.info("generated OTP: {}", otp);
        assertTrue(otp > 0);
    }

    @Test
    public void getOtp() {
        String s = "Hello";
        long otp = otpService.generateOTP(s.hashCode());
        String optString = String.valueOf(otp);
        otp = Long.parseLong(optString);
        logger.info("generated OTP: {}", otp);
        assertTrue(otpService.checkOtp(otp));
    }

    @Test
    public void getOtpFail() throws InterruptedException {
        String s = "Hello";
        long otp = otpService.generateOTP(s.hashCode());
        logger.info("generated OTP: {}", otp);
        Thread.sleep(TimeUnit.SECONDS.toMillis(6));
        assertFalse(otpService.checkOtp(otp));
    }

    @Test
    public void getViewActualOTP1() {
        otpService.generateOTP("123".hashCode());
        otpService.generateOTP("345".hashCode());
        otpService.generateOTP("567".hashCode());
        Map<Integer, Long> actualOTP = otpService.getViewActualOTP();
        for (Map.Entry<Integer, Long> entry : actualOTP.entrySet()) {
            logger.debug(entry.getKey() + ":" + entry.getValue());
        }
        assertEquals(3, actualOTP.size());
    }

    @Test
    public void getViewActualOTP2() {
        String s = "123";
        otpService.generateOTP(s.hashCode());
        otpService.generateOTP(s.hashCode());
        otpService.generateOTP(s.hashCode());
        Map<Integer, Long> actualOTP = otpService.getViewActualOTP();
        for (Map.Entry<Integer, Long> entry : actualOTP.entrySet()) {
            logger.debug(entry.getKey() + ":" + entry.getValue());
        }
        assertEquals(1, actualOTP.size());
    }

}

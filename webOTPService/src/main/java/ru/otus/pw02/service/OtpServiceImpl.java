package ru.otus.pw02.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OtpServiceImpl implements OtpService {

    private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);

    private LoadingCache<Integer, Long> otpCache;

    public OtpServiceImpl(long duration, TimeUnit unit) {
        super();
        logger.info("duration:{} unit:{}", duration, unit);
        otpCache = CacheBuilder.newBuilder().
                expireAfterWrite(duration, unit).build(new CacheLoader<>() {
            @Override
            public Long load(Integer integer) {
                return 0L;
            }
        });
    }

    @Override
    public long generateOTP(int hash) {
        long otp = new Date().getTime() / TimeUnit.SECONDS.toMillis(30);
        otpCache.put(hash, otp);
        return otp;
    }

    @Override
    public boolean checkOtp(long otp) {
        try {
            return otpCache.asMap().containsValue(otp);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Map<Integer, Long> getViewActualOTP() {
        Map<Integer, Long> otpMap = new HashMap<>();
        if (otpCache.size() > 0) {
            otpMap.putAll(otpCache.asMap());
        }
        return otpMap;
    }
}

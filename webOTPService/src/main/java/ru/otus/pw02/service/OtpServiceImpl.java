package ru.otus.pw02.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Date;
import java.util.concurrent.TimeUnit;


public class OtpServiceImpl implements OtpService{

    private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);

    private LoadingCache<String, Long> otpCache;

    public OtpServiceImpl(Integer expireMinutes){
        super();
        logger.info("expireMinutes: {}",expireMinutes);
        otpCache = CacheBuilder.newBuilder().
                expireAfterWrite(expireMinutes, TimeUnit.MINUTES).build(new CacheLoader<>() {
            @Override
            public Long load(String s){
                return 0L;
            }
        });
    }

    @Override
    public long generateOTP(String key){
         long otp = new Date().getTime() / TimeUnit.SECONDS.toMillis(30);
        otpCache.put(key, otp);
        return otp;
    }

    @Override
    public long getOtp(String key){
        try{
            return otpCache.get(key);
        }catch (Exception e){
            return 0;
        }
    }
}
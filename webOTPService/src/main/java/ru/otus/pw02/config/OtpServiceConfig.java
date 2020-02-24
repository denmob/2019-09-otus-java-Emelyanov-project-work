package ru.otus.pw02.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.pw02.service.OtpService;
import ru.otus.pw02.service.OtpServiceImpl;

@Configuration
@ConfigurationProperties(prefix="otp")
@PropertySource("settings.yml")
public class OtpServiceConfig {

    @Value("${expireMin}")
    private int expireMin;

    @Bean
    public OtpService otpService() {
        return new OtpServiceImpl(expireMin);
    }
}

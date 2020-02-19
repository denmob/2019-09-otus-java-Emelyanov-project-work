package ru.otus.pw01.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix="db.phones")
public class AllowedUserPhonesConfig {

    @Value("${allowUserPhoneNumber1}")
    private String allowUserPhoneNumber1;

    @Value("${allowUserPhoneNumber2}")
    private String allowUserPhoneNumber2;

    @Value("${allowUserPhoneNumber3}")
    private String allowUserPhoneNumber3;

    public String getAllowUserPhoneNumber1() {
        return allowUserPhoneNumber1;
    }

    public String getAllowUserPhoneNumber2() {
        return allowUserPhoneNumber2;
    }

    public String getAllowUserPhoneNumber3() {
        return allowUserPhoneNumber3;
    }
}

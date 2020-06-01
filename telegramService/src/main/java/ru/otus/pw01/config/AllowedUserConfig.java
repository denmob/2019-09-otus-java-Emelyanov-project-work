package ru.otus.pw01.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "db.user.allow")
@PropertySource("settings.yml")
public class AllowedUserConfig {

    @Value("${name1}")
    private String name1;

    @Value("${phone1}")
    private String phone1;

    @Value("${name2}")
    private String name2;

    @Value("${phone2}")
    private String phone2;

    @Value("${name3}")
    private String name3;

    @Value("${phone3}")
    private String phone3;

    public String getName1() {
        return name1;
    }

    public String getPhone1() {
        return phone1;
    }

    public String getName2() {
        return name2;
    }

    public String getPhone2() {
        return phone2;
    }

    public String getName3() {
        return name3;
    }

    public String getPhone3() {
        return phone3;
    }
}

package ru.otus.pw01.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties("telegram")
@PropertySource("settings.yml")
public class TelegramConfig {

    public String getRegistrationButton() {
        return registrationButton;
    }

    public String getGenerateOTPButton() {
        return generateOTPButton;
    }

    public String getBotToken() {
        return botToken;
    }

    public String getBotName() {
        return botName;
    }

    @Value("${registrationButton}")
    private String registrationButton;

    @Value("${generateOTPButton}")
    private String generateOTPButton;

    @Value("${botToken}")
    private String botToken;

    @Value("${botName}")
    private String botName;


}
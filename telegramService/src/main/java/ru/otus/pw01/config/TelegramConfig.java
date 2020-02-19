package ru.otus.pw01.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties("telegram")
@PropertySource("settings.yml")
public class TelegramConfig {

    public String getBotToken() {
        return botToken;
    }

    public String getBotName() {
        return botName;
    }

    @Value("${registrationChatMessage}")
    private String registrationChatMessage;

    @Value("${registrationButtonText}")
    private String registrationButtonText;

    @Value("${generateOTPChatMessage}")
    private String generateOTPChatMessage;

    @Value("${generateOTPButtonText}")
    private String generateOTPButtonText;

    @Value("${botToken}")
    private String botToken;

    @Value("${botName}")
    private String botName;


    public String getRegistrationChatMessage() {
        return registrationChatMessage;
    }

    public String getRegistrationButtonText() {
        return registrationButtonText;
    }

    public String getGenerateOTPChatMessage() {
        return generateOTPChatMessage;
    }


    public String getGenerateOTPButtonText() {
        return generateOTPButtonText;
    }



}
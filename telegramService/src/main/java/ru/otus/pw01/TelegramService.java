package ru.otus.pw01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.ApiContextInitializer;

@Configuration
@SpringBootApplication
public class TelegramService {

    private static Logger logger = LoggerFactory.getLogger(TelegramService.class);

    public static void main(String[] args)  {
        ApiContextInitializer.init();
        try {
            SpringApplication.run(TelegramService.class, args);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }
}

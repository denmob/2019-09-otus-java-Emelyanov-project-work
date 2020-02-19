package ru.otus.pw01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.ApiContextInitializer;

@Configuration
@SpringBootApplication
public class TelegramService {
    public static void main(String[] args)  {
        ApiContextInitializer.init();
        try {
            SpringApplication.run(TelegramService.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

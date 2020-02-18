package ru.otus.pw02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
@Configuration
@SpringBootApplication
public class WebOtpService {

    public static void main(String[] args)  {
        try {
            SpringApplication.run(WebOtpService.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

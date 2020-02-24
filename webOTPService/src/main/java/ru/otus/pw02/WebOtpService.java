package ru.otus.pw02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class WebOtpService {

    private static Logger logger = LoggerFactory.getLogger(WebOtpService.class);

    public static void main(String[] args)  {
        try {
            SpringApplication.run(WebOtpService.class, args);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }

}

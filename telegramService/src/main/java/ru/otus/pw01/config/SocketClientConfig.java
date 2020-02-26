package ru.otus.pw01.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.pw.library.mq.MqHandler;
import ru.otus.pw.library.mq.MqHandlerImpl;
import ru.otus.pw.library.service.MqService;
import ru.otus.pw01.controller.TelegramController;
import ru.otus.pw01.service.MqServiceImpl;
import ru.otus.pw01.sokets.SocketClient;
import ru.otus.pw01.sokets.SocketClientImpl;

@Configuration
@PropertySource("settings.yml")
public class SocketClientConfig {

    @Value("${socketHost}")
    private String socketHost;

    @Value("${socketPort}")
    private int socketPort;

    @Value("${httpHost}")
    private String httpHost;

    @Value("${httpPort}")
    private int httpPort;

    @Value("${rabbitMqHost}")
    private String rabbitMqHost;

    @Value("${rabbitMqPort}")
    private int rabbitMqPort;

    @Value("${queueDataName:telegramService}")
    private String queueDataName;

    @Bean
    public MqHandler mqHandler() {
        return new MqHandlerImpl(rabbitMqHost,rabbitMqPort,queueDataName);
    }

    @Bean
    public SocketClient socketClient(MqHandler mqHandler) {
        SocketClient socketClient = new SocketClientImpl(socketHost,socketPort,mqHandler);
        socketClient.start();
        return socketClient;
    }

    @Bean
    public MqService mqService(MqHandler mqHandler, TelegramController telegramController) {
        MqService mqService = new MqServiceImpl(mqHandler,telegramController,httpHost,httpPort);
        mqService.start();
        return mqService;
    }
}

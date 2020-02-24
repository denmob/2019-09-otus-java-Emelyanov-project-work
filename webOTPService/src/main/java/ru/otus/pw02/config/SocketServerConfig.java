package ru.otus.pw02.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.pw.library.mq.MqHandler;
import ru.otus.pw.library.mq.MqHandlerImpl;
import ru.otus.pw.library.service.MqService;
import ru.otus.pw02.service.MqServiceImpl;
import ru.otus.pw02.service.OtpService;
import ru.otus.pw02.sockets.SocketServer;
import ru.otus.pw02.sockets.SocketServerImpl;

@Configuration
@PropertySource("settings.yml")
public class SocketServerConfig {

    @Value("${socketPort}")
    private int socketPort;

    @Value("${rabbitMqHost}")
    private String rabbitMqHost;

    @Value("${rabbitMqPort}")
    private int rabbitMqPort;

    @Value("${queueDataName:otpService}")
    private String queueDataName;

    @Bean
    public MqHandler mqHandler() {
        return new MqHandlerImpl(rabbitMqHost,rabbitMqPort,queueDataName);
    }

    @Bean
    public SocketServer socketServer(MqHandler mqHandler) {
        SocketServer server = new SocketServerImpl(socketPort,mqHandler);
        server.start();
        return server;
    }

    @Bean
    public MqService mqService(SocketServer socketServer, MqHandler mqHandler, OtpService otpService) {
        MqService mqService = new MqServiceImpl(socketServer,mqHandler,otpService);
        mqService.start();
        return mqService;
    }


}

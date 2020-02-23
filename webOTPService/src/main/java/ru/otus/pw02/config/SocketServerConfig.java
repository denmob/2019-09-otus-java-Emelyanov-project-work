package ru.otus.pw02.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.pw.library.mq.MqHandlerImpl;
import ru.otus.pw02.sockets.SocketServer;
import ru.otus.pw02.sockets.SocketServerImpl;

@Configuration
@ConfigurationProperties(prefix="socket")
@PropertySource("settings.yml")
public class SocketServerConfig {

    @Value("${host:localhost}")
    private String host;

    @Value("${port:8000}")
    private int port;

    @Value("${clientsNumber:1}")
    private int clientsNumber;

    @Value("${queueDataName:OPT_SERVICE}")
    private String queueDataName;

    @Bean
    public MqHandlerImpl serverHandler() {
        return  new MqHandlerImpl(host,queueDataName);
    }

    @Bean
    public SocketServer socketServer(MqHandlerImpl serverHandler) {
        SocketServer server = new SocketServerImpl(port, clientsNumber,serverHandler);
        server.start();
        return server;
    }

}

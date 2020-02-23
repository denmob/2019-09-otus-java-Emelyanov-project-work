package ru.otus.pw01.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.pw01.sokets.SocketClient;
import ru.otus.pw01.sokets.SocketClientImpl;

@Configuration
@ConfigurationProperties(prefix="socket.server")
@PropertySource("settings.yml")
public class SocketClientConfig {

    @Value("${host}")
    private String host;

    @Value("${port}")
    private int port;

    @Bean
    public SocketClient socketClient() {
        SocketClient socketClient = new SocketClientImpl(host,port);
        socketClient.start();
        return socketClient;
    }
}

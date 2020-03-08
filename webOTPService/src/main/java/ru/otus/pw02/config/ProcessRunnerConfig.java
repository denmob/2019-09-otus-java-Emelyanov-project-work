package ru.otus.pw02.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.pw02.runner.ProcessRunnerImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
@ConfigurationProperties(prefix="runner")
@PropertySource("settings.yml")
public class ProcessRunnerConfig {

    private static final Logger logger = LoggerFactory.getLogger(ProcessRunnerConfig.class);

    @Value("${telegramServiceStartCommand}")
    private String telegramServiceStartCommand;

    @Value("${clientStartDelaySec:5}")
    private int clientStartDelaySec;

    @Bean
    public void runClients() {
        logger.debug("telegramServiceStartCommand: {}", telegramServiceStartCommand);
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        startClient(executorService, getCommands());
        executorService.shutdown();
    }

    private void startClient(ScheduledExecutorService executorService, List<String> commands) {
        for (String command : commands) {
            executorService.schedule(() -> {
                if (!command.isEmpty()) {
                    try {
                        new ProcessRunnerImpl().start(command);
                        logger.debug("run command: {}",command);
                    } catch (IOException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }, clientStartDelaySec, TimeUnit.SECONDS);
        }
    }

    private  List<String> getCommands() {
        List<String> commands = new ArrayList<>();
        commands.add(telegramServiceStartCommand);
        return commands;
    }
}

package ru.otus.pw02.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ProcessRunnerImpl implements ProcessRunner {

    private static Logger logger = LoggerFactory.getLogger(ProcessRunnerImpl.class);
    private Process process;

    @Override
    public void start(String command) throws IOException {
        process = runProcess(command);
        logger.info("process info: {} isAlive:{}", process.info(), process.isAlive());
    }

    @Override
    public void stop() {
        process.destroy();
    }

    private Process runProcess(String command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        processBuilder.redirectErrorStream(true);
        process = processBuilder.start();
        logger.info("process info: {} isAlive:{}", process.info(), process.isAlive());
        return process;
    }
}

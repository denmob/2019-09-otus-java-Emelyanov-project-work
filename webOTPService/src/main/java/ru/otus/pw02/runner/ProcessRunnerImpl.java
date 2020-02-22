package ru.otus.pw02.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProcessRunnerImpl implements ProcessRunner {
    private static Logger logger = LoggerFactory.getLogger(ProcessRunnerImpl.class);

    private final StringBuffer out = new StringBuffer();
    private Process process;

    @Override
    public void start(String command) throws IOException {
        process = runProcess(command);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(),e);
        }
        logger.info("process info: {} isAlive:{}", process.info(), process.isAlive() );
    }

    @Override
    public void stop() {
        process.destroy();
    }

    private Process runProcess(String command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        processBuilder.redirectErrorStream(true);
        process = processBuilder.start();

        StreamListener output = new StreamListener(process.getInputStream(), "OUTPUT");
        output.start();

        logger.info("process info: {} isAlive:{}", process.info(), process.isAlive() );
        return process;
    }


    private class StreamListener extends Thread {
        private final InputStream inputStream;
        private final String type;

        private StreamListener(InputStream is, String type) {
            this.inputStream = is;
            this.type = type;
        }

        @Override
        public void run() {
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)){
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while((line = bufferedReader.readLine()) !=  null){
                    out.append(type).append('>').append(line).append("\n");
                }
            } catch (IOException e) {
                logger.error(e.getMessage(),e);
            }
        }
    }
}

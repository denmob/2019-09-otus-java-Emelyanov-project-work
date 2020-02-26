package ru.otus.pw02.runner;

import java.io.IOException;

public interface ProcessRunner {

    void start(String command) throws IOException;
    void stop();
}

package ru.otus.pw.library.service;

import ru.otus.pw.library.mesages.MessageTransport;

public interface MqService {

    void start();

    void stop();

    void handleMessage(MessageTransport messageTransport);
}

package ru.otus.pw02.mq;

import java.io.IOException;

public interface MqHandler {

    void putToQueue(byte[] message);

    byte[] getFromQueue();

    int getMessageCount();
}

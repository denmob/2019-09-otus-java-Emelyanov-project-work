package ru.otus.pw.library.mq;



public interface MqHandler {

    void putToQueue(byte[] message);

    byte[] getFromQueue();

    int getMessageCount();
}

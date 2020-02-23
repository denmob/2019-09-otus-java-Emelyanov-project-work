package ru.otus.pw01.sokets;


import ru.otus.pw.library.mesages.MessageTransport;

public interface SocketClient {
    void start();

    void stop();

    void sendMessage(MessageTransport message);

    MessageTransport receiveMessage();
}

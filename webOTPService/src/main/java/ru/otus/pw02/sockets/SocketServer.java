package ru.otus.pw02.sockets;

import ru.otus.pw.library.mesages.MessageTransport;

public interface SocketServer {

    void start();
    void stop();
    void sendMessage(MessageTransport messageTransport);
}

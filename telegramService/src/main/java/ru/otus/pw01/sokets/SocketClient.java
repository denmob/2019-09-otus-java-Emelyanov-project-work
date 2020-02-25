package ru.otus.pw01.sokets;


import org.telegram.telegrambots.meta.api.objects.Contact;
import ru.otus.pw.library.mesages.CommandType;
import ru.otus.pw.library.mesages.MessageTransport;

public interface SocketClient {
    void start();

    void stop();

    void sendMessage(CommandType commandType,String from, Contact contact);

//    MessageTransport receiveMessage();
}

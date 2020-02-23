package ru.otus.pw.library.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.pw.library.mesages.MessageTransport;

import java.io.*;

public class SerializeMessageTransport {

    private static Logger logger = LoggerFactory.getLogger(SerializeMessageTransport.class);

    private SerializeMessageTransport() {
    }

    public static byte[] serializeObject(MessageTransport messageTransport) {
        try {
            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bytesOut);
            oos.writeObject(messageTransport);
            oos.flush();
            byte[] bytes = bytesOut.toByteArray();
            bytesOut.close();
            oos.close();
            return bytes;
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    public static MessageTransport deserializeBytes(byte[] bytes) {
        try {
            ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bytesIn);
            Object obj = ois.readObject();
            ois.close();
        return (MessageTransport) obj;
        } catch (IOException | ClassNotFoundException e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }
}

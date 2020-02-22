package ru.otus.pw02.mq;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class MqHandlerImpl implements MqHandler {

    private static Logger logger = LoggerFactory.getLogger(MqHandlerImpl.class);

    private static final String QUEUE_NAME = "QUEUE_DATA";
    private Channel channel;
    private boolean connectionToQueueAvailable = false;
    private AMQP.Queue.DeclareOk dok;

    public MqHandlerImpl(String host) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
             dok = channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            connectionToQueueAvailable = true;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        logger.info("connectionToQueueAvailable: {}", connectionToQueueAvailable);
    }


    public void putToQueue(byte[] message) {
        if (connectionToQueueAvailable) {
            try {
                channel.basicPublish("", QUEUE_NAME, null, message);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public byte[] getFromQueue() {
        if (connectionToQueueAvailable) {
            try {
                GetResponse r = channel.basicGet(QUEUE_NAME, true);
                if (r != null) {
                    return r.getBody();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }

    public int getMessageCount() {
            return dok.getMessageCount();
    }
}

package ru.otus.pw.library.mq;


import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class MqHandlerImpl implements MqHandler {

    private static Logger logger = LoggerFactory.getLogger(MqHandlerImpl.class);

    private String queueName;
    private Channel channel;
    private boolean connectionToQueueAvailable = false;
    private AMQP.Queue.DeclareOk dok;

    public MqHandlerImpl(String host,String queueName) {
        try {
            this.queueName = queueName;
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
             dok = channel.queueDeclare(queueName, false, false, false, null);
            connectionToQueueAvailable = true;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        logger.info("connectionToQueueAvailable: {}", connectionToQueueAvailable);
    }


    public void putToQueue(byte[] message) {
        if (connectionToQueueAvailable) {
            try {
                channel.basicPublish("", queueName, null, message);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public byte[] getFromQueue() {
        if (connectionToQueueAvailable) {
            try {
                GetResponse r = channel.basicGet(queueName, true);
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

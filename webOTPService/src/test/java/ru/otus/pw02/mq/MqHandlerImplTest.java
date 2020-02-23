package ru.otus.pw02.mq;


import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class MqHandlerImplTest {

    private static Logger logger = LoggerFactory.getLogger(MqHandlerImplTest.class);

    private final MqHandler mqHandler = new MqHandlerImpl("localhost","QUEUE_DATA_OPT_SERVICE");

    private final static int COUNT_MESSAGE_FOR_TEST = 10;

    @Test
    @Order(1)
    public void putToQueue() {
        IntStream.range(0, COUNT_MESSAGE_FOR_TEST).mapToObj(i -> ("hello" + i)).forEach(message -> {
            logger.info(message);
            mqHandler.putToQueue(message.getBytes());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    @Test
    @Order(2)
    public void getFromQueue() {
        IntStream.range(0, COUNT_MESSAGE_FOR_TEST).mapToObj(i -> mqHandler.getFromQueue()).forEach(bytes -> {
            if (bytes != null) {
                logger.info(new String(bytes));
            } else
                logger.debug("No message in queue");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    @Test
    @Order(3)
    public void getMessageCount() {
        logger.info("MessageCount: {}" ,mqHandler.getMessageCount());
        Assert.assertEquals(0, mqHandler.getMessageCount());
    }
}

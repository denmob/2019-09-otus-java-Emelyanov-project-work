package ru.otus.pw02.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.pw.library.mesages.MessageTransport;
import ru.otus.pw.library.misc.SerializeMessageTransport;
import ru.otus.pw.library.mq.MqHandler;
import ru.otus.pw02.service.OtpService;
import ru.otus.pw02.sockets.SocketServer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MqSocketController {

    private static final Logger logger = LoggerFactory.getLogger(MqSocketController.class);

    private static final int MSG_HANDLER_THREAD_LIMIT = 5;

    private final SocketServer socketServer;
    private final MqHandler mqHandler;
    private final OtpService otpService;

    private final AtomicBoolean runFlag = new AtomicBoolean(true);

    private final ExecutorService msgProcessor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setName("msg-processor-thread");
        return thread;
    });

    private final ExecutorService msgHandler = Executors.newFixedThreadPool(MSG_HANDLER_THREAD_LIMIT, new ThreadFactory()
    {
        private final AtomicInteger threadNameSeq = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName("msg-handler-thread-" + threadNameSeq.incrementAndGet());
            return thread;
        }
    });

    public MqSocketController(SocketServer socketServer, MqHandler mqHandler,OtpService otpService) {
        this.socketServer = socketServer;
        this.mqHandler = mqHandler;
        this.otpService = otpService;
        msgProcessor.submit(this::msgProcessor);
    }

    private void msgProcessor() {
        logger.debug("Start msgProcessor");
        while (runFlag.get()) {
            if (mqHandler.getMessageCount() > 0) {
                logger.debug("mqHandler.getFromQueue");
                byte[] data = mqHandler.getFromQueue();
                if (data != null) {
                    MessageTransport messageTransport = SerializeMessageTransport.deserializeBytes(data);
                    if (messageTransport != null) {
                        msgHandler.submit(() -> {
                            logger.debug("msgHandler submit message: {} ", messageTransport);
                            handleMessage(messageTransport);
                        });
                    }
                }
            }
        }
    }

    private void handleMessage(MessageTransport messageTransport) {
        try {
            logger.debug("handleMessage: {} ",messageTransport);
            messageTransport.setData(String.valueOf(otpService.generateOTP(messageTransport.getData())));
            socketServer.sendMessage(messageTransport);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

}

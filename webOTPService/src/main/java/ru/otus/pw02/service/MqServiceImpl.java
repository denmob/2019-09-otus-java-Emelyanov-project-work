package ru.otus.pw02.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.pw.library.mesages.MessageTransport;
import ru.otus.pw.library.misc.SerializeMessageTransport;
import ru.otus.pw.library.mq.MqHandler;
import ru.otus.pw.library.service.MqService;
import ru.otus.pw02.sockets.SocketServer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class MqServiceImpl implements MqService {

    private static final Logger logger = LoggerFactory.getLogger(MqServiceImpl.class);

    private static final int MSG_HANDLER_THREAD_LIMIT = 5;
    private final SocketServer socketServer;
    private final MqHandler mqHandler;
    private final OtpService otpService;
    private final AtomicBoolean runFlag = new AtomicBoolean(true);
    private final ExecutorService msgProcessor = Executors.newScheduledThreadPool(MSG_HANDLER_THREAD_LIMIT);
    private final ExecutorService msgHandler = Executors.newScheduledThreadPool(MSG_HANDLER_THREAD_LIMIT);

    public MqServiceImpl(SocketServer socketServer, MqHandler mqHandler, OtpService otpService) {
        this.socketServer = socketServer;
        this.mqHandler = mqHandler;
        this.otpService = otpService;
    }

    @Override
    public void start() {
        runFlag.set(true);
        msgProcessor.submit(this::msgProcessor);
    }

    @Override
    public void stop() {
        runFlag.set(false);
    }

    private void msgProcessor() {
        while (runFlag.get()) {
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

    @Override
    public void handleMessage(MessageTransport messageTransport) {
        try {
            logger.debug("messageTransportRequest: {} ",messageTransport);
            MessageTransport messageTransportResponse = new MessageTransport(messageTransport.getTo(),messageTransport.getFrom(),messageTransport.getCommand(),
                    String.valueOf(otpService.generateOTP(messageTransport.getData())) );
            logger.debug("messageTransportResponse: {} ",messageTransportResponse);
            socketServer.sendMessage(messageTransportResponse);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

}

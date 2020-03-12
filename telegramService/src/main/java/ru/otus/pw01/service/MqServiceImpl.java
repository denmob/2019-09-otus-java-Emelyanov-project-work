package ru.otus.pw01.service;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.otus.pw.library.message.MessageTransport;
import ru.otus.pw.library.misc.SerializeMessageTransport;
import ru.otus.pw.library.mq.MqHandler;
import ru.otus.pw.library.service.MqService;
import ru.otus.pw01.controller.TelegramController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static ru.otus.pw.library.message.CommandType.SUCCESS_SAVE_USER_DATA;

public class MqServiceImpl implements MqService {

    private static final Logger logger = LoggerFactory.getLogger(MqServiceImpl.class);
    private static final int MSG_HANDLER_THREAD_LIMIT = 5;
    private final MqHandler mqHandler;
    private final String httpHost;
    private final int httpPort;
    private TelegramController telegramController;
    private final AtomicBoolean runFlag = new AtomicBoolean(true);
    private final ExecutorService msgProcessor = Executors.newScheduledThreadPool(MSG_HANDLER_THREAD_LIMIT);
    private final ExecutorService msgHandler = Executors.newScheduledThreadPool(MSG_HANDLER_THREAD_LIMIT);

    public MqServiceImpl(MqHandler mqHandler, TelegramController telegramController, String httpHost, int httpPort) {
        this.mqHandler = mqHandler;
        this.httpHost = httpHost;
        this.httpPort = httpPort;
        this.telegramController = telegramController;
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
        logger.debug("Start msgProcessor");
        while (runFlag.get()) {
            byte[] data = mqHandler.getFromQueue();
            if (data != null) {
                MessageTransport messageTransport = SerializeMessageTransport.deserializeByteArrayToMessageTransport(data);
                if (messageTransport != null) {
                    msgHandler.execute(() -> {
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
            SendMessage message = new SendMessage();
            message.setChatId(messageTransport.getTo());
            switch (messageTransport.getCommand()) {
                case SUCCESS_GENERATE_OTP:
                    long otp = new Gson().fromJson((String) messageTransport.getData(), Long.class);
                    String url = String.format("http://%s:%s/login?otpValue=%S", httpHost, httpPort, otp);
                    message.setText(url);
                    break;
                case SUCCESS_SAVE_USER_DATA:
                    logger.debug("handleMessage messageTransport with {}", SUCCESS_SAVE_USER_DATA.getValue());
                    break;
                case RESPONSE_WITH_ERROR:
                    String responseMessage = new Gson().fromJson((String) messageTransport.getData(), String.class);
                    message.setText(responseMessage);
                    break;
                default:
                    logger.error("Unexpected value: {}", messageTransport.getCommand());
                    throw new IllegalStateException("Unexpected value: " + messageTransport.getCommand());
            }
            telegramController.sendToUser(message);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

}

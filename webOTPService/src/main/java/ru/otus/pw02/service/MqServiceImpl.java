package ru.otus.pw02.service;

import com.google.gson.Gson;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.pw.library.mesages.CommandType;
import ru.otus.pw.library.mesages.MessageTransport;
import ru.otus.pw.library.misc.SerializeMessageTransport;
import ru.otus.pw.library.mq.MqHandler;
import ru.otus.pw.library.service.MqService;
import ru.otus.pw.library.model.UserData;
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
    private final UserDataService userDataService;
    private final AtomicBoolean runFlag = new AtomicBoolean(true);
    private final ExecutorService msgProcessor = Executors.newScheduledThreadPool(MSG_HANDLER_THREAD_LIMIT);
    private final ExecutorService msgHandler = Executors.newScheduledThreadPool(MSG_HANDLER_THREAD_LIMIT);

    public MqServiceImpl(SocketServer socketServer, MqHandler mqHandler, OtpService otpService,UserDataService userDataService) {
        this.socketServer = socketServer;
        this.mqHandler = mqHandler;
        this.otpService = otpService;
        this.userDataService = userDataService;
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
            MessageTransport messageTransportResponse;
            logger.debug("messageTransportRequest: {} ",messageTransport);
            logger.debug("messageTransport.getCommand(): {} ",messageTransport.getCommand());
            switch (messageTransport.getCommand()){
                case GENERATE_OTP:{
                   UserData userData =userDataService.findUserDataByUserId(Long.parseLong(messageTransport.getFrom()));
                    if (userData != null) {
                         messageTransportResponse =
                                new MessageTransport(messageTransport.getTo(), messageTransport.getFrom(), CommandType.SUCCESS_GENERATE_OTP);
                        long otp = otpService.generateOTP(userData.hashCode());
                        messageTransportResponse.setData(new Gson().toJson(otp));
                    } else {
                         messageTransportResponse =
                                new MessageTransport(messageTransport.getTo(), messageTransport.getFrom(), CommandType.RESPONSE_WITH_ERROR);
                        messageTransportResponse.setData("UserData not found.");
                    }
                    break;
                }
                case SAVE_USER_DATA:{
                    try {
                        UserData userData = new Gson().fromJson((String) messageTransport.getData(), UserData.class);
                        if (userData != null) {
                            logger.debug("userData: {}",userData);
                            userDataService.saveUserDataIfNotExist(userData);
                            messageTransportResponse =
                                    new MessageTransport(messageTransport.getTo(), messageTransport.getFrom(), CommandType.SUCCESS_SAVE_USER_DATA);
                        } else {
                            messageTransportResponse =
                                    new MessageTransport(messageTransport.getTo(), messageTransport.getFrom(), CommandType.RESPONSE_WITH_ERROR);
                            messageTransportResponse.setData("messageTransport.Data is not userData");
                        }
                    }catch (Exception e) {
                        logger.error(e.getMessage(),e);
                        messageTransportResponse =
                                new MessageTransport(messageTransport.getTo(), messageTransport.getFrom(), CommandType.RESPONSE_WITH_ERROR);
                        messageTransportResponse.setData(e.getMessage());
                    }
                    break;
                }
                default:
                    messageTransportResponse =
                            new MessageTransport(messageTransport.getTo(), messageTransport.getFrom(), CommandType.RESPONSE_WITH_ERROR);
                    messageTransportResponse.setData("Unexpected value: " + messageTransport.getCommand());
            }

            logger.debug("messageTransportResponse: {} ",messageTransportResponse);
            socketServer.sendMessage(messageTransportResponse);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}

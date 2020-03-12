package ru.otus.pw01.sokets;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Contact;
import ru.otus.pw.library.message.CommandType;
import ru.otus.pw.library.message.MessageTransport;
import ru.otus.pw.library.misc.SerializeMessageTransport;
import ru.otus.pw.library.mq.MqHandler;
import ru.otus.pw.library.handshake.HandShake;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SocketClientImpl implements SocketClient {
    private static Logger logger = LoggerFactory.getLogger(SocketClientImpl.class);

    private Socket clientSocket;
    private final MqHandler mqHandler;
    private final ExecutorService executorServer = Executors.newSingleThreadExecutor();
    private final ExecutorService executorSender = Executors.newSingleThreadExecutor();

    public SocketClientImpl(String host, int port, MqHandler mqHandler) {
        this.mqHandler = mqHandler;
        try {
            clientSocket = new Socket(host, port);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void registrationSocketServer() {
        try {
            if (clientSocket.isConnected()) {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String jsonParam = new Gson().toJson(HandShake.REGISTRATION_VALUE.getValue());
                logger.info("send to OTPService: {}", jsonParam);
                out.println(jsonParam);
                String response = new Gson().fromJson(in.readLine(), String.class);
                if (response.equals(HandShake.REGISTRATION_SUCCESS.getValue())) {
                    logger.info("client registration successful");
                    executorServer.execute(this::clientHandler);
                } else {
                    logger.error(HandShake.REGISTRATION_FAIL.getValue());
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private void clientHandler() {
        try {
            while (clientSocket.isConnected()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String resp = in.readLine();
                if (resp != null) {
                    logger.info("server response: {}", resp);
                    MessageTransport messageOut = new Gson().fromJson(resp, MessageTransport.class);
                    mqHandler.putToQueue(SerializeMessageTransport.serializeMessageTransportToByteArray(messageOut));
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void start() {
        registrationSocketServer();
    }

    @Override
    public void stop() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void sendMessage(CommandType commandType, String from, Contact contact) {
        logger.debug("execute sendMessage commandType:{} from:{}, contact:{}", commandType, from, contact);
        executorSender.execute(() -> {
            if (this.clientSocket != null) {
                try {
                    if (clientSocket.isConnected()) {
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        MessageTransport messageTransport;
                        switch (commandType) {
                            case SAVE_USER_DATA:
                                messageTransport = new MessageTransport(from, "otpService", commandType);
                                messageTransport.setData(new Gson().toJson(contact));
                                break;
                            case GENERATE_OTP:
                                messageTransport = new MessageTransport(from, "otpService", commandType);
                                break;
                            default: {
                                logger.error("Unexpected value:{} ", commandType);
                                throw new IllegalStateException("Unexpected value: " + commandType);
                            }
                        }
                        String json = new Gson().toJson(messageTransport);
                        logger.debug("send json: {} to socket server", json);
                        out.println(json);
                    } else logger.error("Socket server not found");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            } else logger.error("Socket client not registered");
        });
    }


}

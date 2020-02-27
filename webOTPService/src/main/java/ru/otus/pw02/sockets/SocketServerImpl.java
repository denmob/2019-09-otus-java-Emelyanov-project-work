package ru.otus.pw02.sockets;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.pw.library.message.MessageTransport;
import ru.otus.pw.library.mq.MqHandler;
import ru.otus.pw.library.handshake.HandShake;
import ru.otus.pw.library.misc.SerializeMessageTransport;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServerImpl implements SocketServer {
  private static Logger logger = LoggerFactory.getLogger(SocketServerImpl.class);
  private final int socketPort;
  private final ExecutorService executorServer = Executors.newScheduledThreadPool(5);
  private boolean running = false;
  private final MqHandler mqHandler;
  private Socket socketClient;

  public SocketServerImpl(int socketPort, MqHandler mqHandler) {
      this.socketPort = socketPort;
      this.mqHandler = mqHandler;
      this.executorServer.execute(this::run);
  }

  private void run() {
    logger.info("ServerSocket port: {}",socketPort);
    try (ServerSocket serverSocket = new ServerSocket(socketPort)) {
      while (running) {
        Socket clientSocket = serverSocket.accept();
        executorServer.execute(() -> clientHandler(clientSocket));
      }
    } catch (Exception ex) {
      logger.error("error", ex);
    }
  }

  private String registrationSocketClient(Socket clientSocket, String inputData) {
    String fromJson = new Gson().fromJson(inputData, String.class);
    String responseMessage;
    if (fromJson.equals(HandShake.REGISTRATION_VALUE.getValue())) {
      this.socketClient = clientSocket;
      logger.debug("client registered with param: {}", inputData);
      responseMessage = new Gson().toJson(HandShake.REGISTRATION_SUCCESS.getValue());
    } else {
      logger.debug("Invalid handshake value");
      responseMessage = new Gson().toJson(HandShake.REGISTRATION_FAIL.getValue());
    }
    return responseMessage;
  }

  private void clientHandler(Socket clientSocket) {
    logger.debug("clientHandler ");
    try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
         BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        logger.debug("input message: {} ", inputLine);
        if (this.socketClient == null) {
          String responseMessage = registrationSocketClient(clientSocket, inputLine);
          logger.debug("responseMessage: {}",responseMessage);
          out.println(responseMessage);
        } else {
          MessageTransport messageTransport = new Gson().fromJson(inputLine, MessageTransport.class);
          logger.debug("messageTransport: {}", messageTransport);
          mqHandler.putToQueue(SerializeMessageTransport.serializeMessageTransportToByteArray(messageTransport));
        }
      }
    } catch(Exception ex){
        logger.error(ex.getMessage(), ex);
    }
  }

    @Override
    public void start() {
      running = true;
    }

    @Override
    public void stop() {
      this.socketClient = null;
      running = false;
    }

    @Override
    public void sendMessage(MessageTransport messageTransport) {
      if (this.socketClient != null) {
        try {
          if (socketClient.isConnected()) {
            PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);
            String json = new Gson().toJson(messageTransport);
            out.println(json);
          }
        } catch (Exception ex) {
          logger.error(ex.getMessage(), ex);
        }
      } else logger.error("Socket client not registered");
    }
}

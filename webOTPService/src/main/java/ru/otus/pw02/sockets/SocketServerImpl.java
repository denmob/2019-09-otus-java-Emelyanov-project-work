package ru.otus.pw02.sockets;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.pw.library.mesages.MessageTransport;
import ru.otus.pw.library.mq.MqHandler;
import ru.otus.pw.library.mq.MqHandlerImpl;
import ru.otus.pw.library.secret.HandShake;
import ru.otus.pw.library.misc.SerializeMessageTransport;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServerImpl implements SocketServer {
  private static Logger logger = LoggerFactory.getLogger(SocketServerImpl.class);

  private final int socketPort;
  private final ExecutorService executorServer = Executors.newSingleThreadExecutor();
  private boolean running = false;
  private final Object monitor = new Object();
  private final MqHandler serverHandler;
  private Socket socketClient;

  public SocketServerImpl(int socketPort, MqHandler serverHandler) {
    this.socketPort = socketPort;
    this.serverHandler = serverHandler;
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
    synchronized (monitor) {
      String fromJson = new Gson().fromJson(inputData, String.class);
      String responseMessage;
      if (fromJson.equals(HandShake.REGISTRATION_VALUE.getValue())) {
        this.socketClient = clientSocket;
        responseMessage = String.format("client registered with param: %s", inputData);
        logger.debug(responseMessage);
      } else {
        responseMessage = "Invalid handshake value";
        logger.debug(responseMessage);
      }
      responseMessage = new Gson().toJson(responseMessage);
      return responseMessage;
    }
  }

  private void clientHandler(Socket clientSocket) {
    try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
         BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        logger.debug("input message: {} ", inputLine);
        if (socketClient != null) {
          out.println(registrationSocketClient(clientSocket, inputLine));
        } else {
          MessageTransport messageTransport = new Gson().fromJson(inputLine, MessageTransport.class);
          logger.debug("messageTransport: {}", messageTransport);
          serverHandler.putToQueue(SerializeMessageTransport.serializeObject(messageTransport));
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

package ru.otus.pw01.sokets;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.pw.library.mesages.MessageTransport;
import ru.otus.pw.library.misc.SerializeMessageTransport;
import ru.otus.pw.library.mq.MqHandler;
import ru.otus.pw.library.secret.HandShake;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SocketClientImpl implements SocketClient {
  private static Logger logger = LoggerFactory.getLogger(SocketClientImpl.class);

  private Socket clientSocket;
  private final MqHandler mqHandler;
  private final ExecutorService executorServer = Executors.newSingleThreadExecutor();

  public SocketClientImpl(String host, int port, MqHandler mqHandler) {
    this.mqHandler = mqHandler;
    try {
      clientSocket = new Socket(host, port);
    } catch (IOException e) {
      logger.error(e.getMessage(),e);
    }
  }

  private void registrationToMs() {
    try {
      if (clientSocket.isConnected()) {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String jsonParam = new Gson().toJson(HandShake.REGISTRATION_VALUE);
        logger.info("send to MS name: {}",jsonParam);
        out.println(jsonParam);
        boolean answerMs = Boolean.parseBoolean(in.readLine());
        if (answerMs) {
          executorServer.execute(this::run);
          logger.info("client registration successful");
        }
      }
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
    }
  }

  private void run() {
    try {
      while (clientSocket.isConnected()) {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        MessageTransport msg = SerializeMessageTransport.deserializeBytes(mqHandler.getFromQueue());
        String json =  new Gson().toJson(msg);
        logger.info("sending to server {}",json);
        out.println(json);
        String resp = in.readLine();
        logger.info("server response: {}", resp);
        MessageTransport messageOut = new Gson().fromJson(resp, MessageTransport.class);
        mqHandler.putToQueue(SerializeMessageTransport.serializeObject(messageOut));
      }
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
    }
  }

  @Override
  public void start() {
    executorServer.execute(this::registrationToMs);
  }

  @Override
  public void stop() {
    try {
      clientSocket.close();
    } catch (IOException e) {
      logger.error(e.getMessage(),e);
    }
  }

  @Override
  public void sendMessage(MessageTransport message) {
      mqHandler.putToQueue(SerializeMessageTransport.serializeObject(message));
  }

  @Override
  public MessageTransport receiveMessage() {
      return  SerializeMessageTransport.deserializeBytes(mqHandler.getFromQueue());
  }
}

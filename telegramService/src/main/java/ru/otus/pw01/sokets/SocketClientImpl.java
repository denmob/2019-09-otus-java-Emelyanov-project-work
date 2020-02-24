package ru.otus.pw01.sokets;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.pw.library.mesages.MessageTransport;
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

  public SocketClientImpl(String host, int port, MqHandler mqHandler) {
    this.mqHandler = mqHandler;
    try {
      clientSocket = new Socket(host, port);
    } catch (IOException e) {
      logger.error(e.getMessage(),e);
    }
  }

  private void registrationOnOTPService() {
    try {
      if (clientSocket.isConnected()) {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String jsonParam = new Gson().toJson(HandShake.REGISTRATION_VALUE.getValue());
        logger.info("send to OTPService: {}",jsonParam);
        out.println(jsonParam);
        String response = new Gson().fromJson(in.readLine(), String.class);
        if (response.equals(HandShake.REGISTRATION_SUCCESS.getValue())) {
          logger.info("client registration successful");
          executorServer.execute(this::response);
        } else {
          logger.error(HandShake.REGISTRATION_FAIL.getValue());
        }
      }
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
    }
  }

  private void response() {
    try {
      while (clientSocket.isConnected()) {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String resp = in.readLine();
        if (resp!= null) {
          logger.info("server response: {}", resp);
          MessageTransport messageOut = new Gson().fromJson(resp, MessageTransport.class);
          mqHandler.putToQueue(SerializeMessageTransport.serializeObject(messageOut));
        }
      }
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
    }
  }

  @Override
  public void start() {
    registrationOnOTPService();
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
  public void sendMessage(MessageTransport messageTransport) {
    if (this.clientSocket != null) {
      try {
        if (clientSocket.isConnected()) {
          PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
          String json = new Gson().toJson(messageTransport);
          out.println(json);
        }
      } catch (Exception ex) {
        logger.error(ex.getMessage(), ex);
      }
    } else logger.error("Socket client not registered");
  }

  @Override
  public MessageTransport receiveMessage() {
      return  SerializeMessageTransport.deserializeBytes(mqHandler.getFromQueue());
  }
}

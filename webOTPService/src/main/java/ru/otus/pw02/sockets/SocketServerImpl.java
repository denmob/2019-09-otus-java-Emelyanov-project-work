package ru.otus.pw02.sockets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.pw02.mq.MqHandlerImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServerImpl implements SocketServer {
  private static Logger logger = LoggerFactory.getLogger(SocketServerImpl.class);

  private final int socketPort;
  private final ExecutorService executorServer;
  private boolean running = false;
  private final Object monitor = new Object();
  private final MqHandlerImpl serverHandler;
  private final Map<String,Socket> socketClient = new ConcurrentHashMap<>();

  public SocketServerImpl(int socketPort,int clientsNumber, MqHandlerImpl serverHandler) {
    this.socketPort = socketPort;
    this.executorServer = Executors.newScheduledThreadPool(clientsNumber);
    this.executorServer.execute(this::run);
    this.serverHandler = serverHandler;
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

  private void clientHandler(Socket clientSocket) {
    try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        logger.debug("input message: {} ", inputLine);

        if (!socketClient.containsValue(clientSocket)) {
          synchronized (monitor) {
            socketClient.put(inputLine, clientSocket);
            String responseMessage = String.format("client registered with param: %s",inputLine);
            logger.debug(responseMessage);
            serverHandler.putToQueue(responseMessage.getBytes());
          }
        } else {
          serverHandler.putToQueue("Hello from server".getBytes());
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
    running = false;
  }
}

package ru.geekbrains.april.chat.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final Logger log = LogManager.getLogger(Server.class);
    private int port;
    private List<ClientHandler> clients;
    private AuthenticationProvider authenticationProvider;
    // private DbAuthenticationProvider authenticationProvider;

    public AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }

    public Server(int port) {
        this.port = port;
        this.clients = new ArrayList<>();
        this.authenticationProvider = new DbAuthenticationProvider();
        this.authenticationProvider.init();
        //  this.authenticationProvider = new DbAuthenticationProvider();

        //   this.authenticationProvider.connect();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
//
            log.info("Сервер запущен");

            System.out.println("Сервер запущен на порту " + port);
            while (true) {
                System.out.println("Ждем нового клиента..");
                Socket socket = serverSocket.accept();
                System.out.println("Клиент подключился");
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
//
            log.throwing(e);
//
            e.printStackTrace();
        } finally {
            this.authenticationProvider.shutdown();
            //  DbAuthenticationProvider.disconnect();
        }
    }

    public synchronized void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
//
        log.info("Клиент " + clientHandler.getUsername() + " подключился");
//
        broadcastMessage("Клиент " + clientHandler.getUsername() + " вошел в чат");
        broadcastClientsList();

    }

    public synchronized void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastMessage("Клиент " + clientHandler.getUsername() + " вышел из чата");
        broadcastClientsList();
    }

    public synchronized void broadcastMessage(String message) {

        for (ClientHandler clientHandler : clients) {
//
            log.info("Клиент прислал сообщение/команду");
//
            clientHandler.sendMessage(message);
        }
    }

    public synchronized void sendPrivateMessage(ClientHandler sender, String receiverUsername, String message) {
        for (ClientHandler client : clients) {
            if (client.getUsername().equals(receiverUsername)) {
                client.sendMessage("От: " + sender.getUsername() + " Сообщение " + message);
                sender.sendMessage("Пользователю: " + receiverUsername + " Сообщение " + message);
                return;
            }
        }
        sender.sendMessage("Невозможно отправить сообщение пользователю : " + receiverUsername + ". Такого пользователя нет в сети.");

    }

    public synchronized boolean isUserOnline(String username) {
        for (ClientHandler clientHandler : clients) {
            if (clientHandler.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void broadcastClientsList() {
        StringBuilder stringBuilder = new StringBuilder("/clients_list ");
        for (ClientHandler client : clients) {
            stringBuilder.append(client.getUsername()).append(" ");
        }
        stringBuilder.setLength(stringBuilder.length() - 1);
        String clientsList = stringBuilder.toString();
        for (ClientHandler clientHandler : clients) {
            clientHandler.sendMessage(clientsList);
        }
    }
}

package ru.geekbrains.april.chat.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;


public class HelloController implements Initializable {
    @FXML
    TextField msgField, loginField;
    @FXML
    PasswordField passwordField;

    @FXML
    TextArea msgArea;


    @FXML
    HBox loginPanel, msgPanel;

    @FXML
    ListView<String> clientsList;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String username;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUsername(null);
    }

    public void setUsername(String username) {
        this.username = username;
        boolean usernameIsNull = username == null;
        loginPanel.setVisible(usernameIsNull);
        loginPanel.setManaged(usernameIsNull);
        msgPanel.setVisible(!usernameIsNull);
        msgPanel.setManaged(!usernameIsNull);
        clientsList.setVisible(!usernameIsNull);
        clientsList.setManaged(!usernameIsNull);
    }

    public void login() {
        if (loginField.getText().isEmpty()) {
            showErrorAlert("Имя пользователя не может быть пустын");
            return;
        }

        if (socket == null || socket.isClosed()) {
            connect();
        }


        try {
            out.writeUTF("/login " + loginField.getText() + " " + passwordField.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        try {
            socket = new Socket("localhost", 8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            Thread thread = new Thread(() -> {
                try {
                    // Цикл авторизации
                    while (true) {
                        String message = in.readUTF();
                        if (message.startsWith("/login_ok ")) {
                            setUsername(message.split("\\s")[1]);
                            break;
                        }
                        if (message.startsWith("/login_failed ")) {
                            String cause = message.split("\\s", 2)[1];
                            msgArea.appendText(cause + "\n");
                        }
                    }
                    // Цикл общения
                    while (true) {
                        String message = in.readUTF();
                        if (message.startsWith("/")) {
                            if (message.startsWith("/exit")) {
                                break;
                            }
                            if (message.startsWith("/clients_list ")) {
                                // /clients_list  Bob Max Jack
                                String[] tokens = message.split("\\s");

                                Platform.runLater(() -> {
                                    clientsList.getItems().clear();
                                    for (int i = 1; i < tokens.length; i++) {
                                        clientsList.getItems().add(tokens[i]);
                                    }
                                });

                            }
                            continue;

                        }
                        msgArea.appendText(message + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    disconnect();
                }
            });
            thread.start();
        } catch (IOException e) {
            showErrorAlert("Невозможно подключиться к серверу");
        }
    }

    public void sendMsg() {
        try {
            out.writeUTF(msgField.getText()); // отправить сообщение полученное из строки ввода
            msgField.clear();         // очистить строку ввода после отправки сообщения
            msgField.requestFocus();  // переключи фокус вот сюда (тут на строку ввода)
        } catch (IOException e) {
            showErrorAlert("Невозможно отправить сообщение");
        }
    }

    private void disconnect() {
        setUsername(null);
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.setTitle("April Chat FX");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
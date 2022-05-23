package ru.geekbrains.april.chat.server;

public interface AuthenticationProvider {
    void init();

    String getNicknameByLoginAndPassword(String login, String password);

    void changeNickname(String oldNickName, String newNickname);
    boolean isNickBusy(String nickname);

    void shutdown();

}

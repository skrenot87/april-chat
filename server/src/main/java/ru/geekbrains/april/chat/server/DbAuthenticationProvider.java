package ru.geekbrains.april.chat.server;

import java.sql.*;

public class DbAuthenticationProvider implements AuthenticationProvider {
    private DbConnection dbConnection;

    @Override
    public void init() {
        dbConnection = new DbConnection();
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        String query = String.format("select nickname from users where login = '%s' and password = '%s';", login, password);
        try (ResultSet rs = dbConnection.getStmt().executeQuery(query)) {
            if (rs.next()) {
                return rs.getString("nickname");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void changeNickname(String oldNickName, String newNickname) {
        String query = String.format("update users set nickname = '%s' where nickname = '$s'", oldNickName, newNickname);
        try {
            // todo есть danger наткнуться на не уникальный никнейм
            dbConnection.getStmt().executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean isNickBusy(String nickname) {
        String query = String.format("select id from users where nickname = '%s';", nickname);
        try (ResultSet rs = dbConnection.getStmt().executeQuery(query)) {
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void shutdown() {
        dbConnection.close();
    }

}

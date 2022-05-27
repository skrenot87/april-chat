package ru.geekbrains.april.chat.client.history;

import javafx.scene.control.TextArea;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryMessage {
    public static void saveHistory(String username, String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(username + "_history.txt"))) {
            writer.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void loadHistory(String username, TextArea msgArea) {
        try (BufferedReader reader = new BufferedReader(new FileReader(username + "_history.txt"))) {
            List<String> list = new ArrayList<>();
            while (true) {
                String str = reader.readLine();
                if (str == null) {
                    break;
                }
                if (list.size() < 100) {
                    list.add(str);
                } else {
                    list.remove(0);
                    list.add(str);
                }
            }
            for (int i = 0; i < list.size(); i++) {
                 msgArea.appendText(list.get(i) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}

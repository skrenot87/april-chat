package ru.geekbrains.april.chat.client.NewArray;

import java.util.Arrays;

public class NewArray {
    public static void main(String[] args) {
        int[] sourceArr = {1, 2, 4, 4, 2, 3, 4, 1, 7};
        int[] sourceArr1 = {2, 5, 6, 8, 9, 7, 5};
        result(sourceArr);
        System.out.println(Arrays.toString(result(sourceArr)));
        System.out.println(checkArray(sourceArr1));
    }

    public static int[] result(int[] sourceArr) throws RuntimeException {
        int number = 4;
        int lastIndex = -1;
        for (int i = 0; i < sourceArr.length; i++) {
            if (sourceArr[i] == number) {
                lastIndex = i + 1;
            }
        }
        if (lastIndex == -1) {
            throw new RuntimeException();
        }
        return Arrays.copyOfRange(sourceArr, lastIndex, sourceArr.length);
    }

    public static boolean checkArray(int[] sourceArr1) {
        int count = 0;
        for (int i = 0; i < sourceArr1.length; i++) {
            if (sourceArr1[i] == 1 || sourceArr1[i] == 4) {
                count++;
            }
        }
        return (count > 0);
    }
}

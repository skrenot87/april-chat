package ru.geekbrains.april.chat.client.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.geekbrains.april.chat.client.NewArray.NewArray;

public class ArrayTests {
    @Test
    public void testCreateArray() {
        int[] arr = new int[]{1, 2, 4, 4, 2, 3, 4, 1, 7};
        Assertions.assertArrayEquals(NewArray.result(arr), new int[]{1, 7});
    }

    @Test
    public void testCreateArray1() {
        Assertions.assertArrayEquals(NewArray.result(new int[]{7, 4, 6, 9}), new int[]{6, 9});
    }

    @Test
    public void testCreateArray2() {
        int[] arr = new int[]{5, 9, 4, 7, 8, 9, 6, 7, 5, 4, 8, 3};
        Assertions.assertArrayEquals(NewArray.result(arr), new int[]{8, 3});
    }

    @Test
    public void testCreateArray3() {
        int[] arr = new int[]{1, 3, 6, 5};
        Assertions.assertThrows(RuntimeException.class, () -> NewArray.result(arr));
    }

    @Test
    public void testCheckArray() {
        int[] arr = new int[]{0, 6, 5, 4, 78, 4, 5};
        Assertions.assertTrue(NewArray.checkArray(arr));
    }
    @Test
    public void testCheckArray2() {
        Assertions.assertTrue(NewArray.checkArray(new int[]{0, 6, 5, 4, 78, 4, 5}));
    }

    @Test
    public void testCheckArray3() {
        int[] arr = new int[]{5, 6, 8, 9, 7, 5, 2, 3, 5, 95, 94};
        Assertions.assertFalse(NewArray.checkArray(arr));
    }
    @Test
    public void testCheckArray4() {
        Assertions.assertFalse(NewArray.checkArray(new int[]{5, 6, 8, 9, 7, 5, 2, 3, 5, 95, 94}));
    }
}

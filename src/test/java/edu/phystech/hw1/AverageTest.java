package edu.phystech.hw1;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class AverageTest {

    // Здесь описаны сигнатуры функции average для трех чисел, нужно переписать сигнатуру, принимающую произвольное
    // количество параметров, то есть вызов функции будет выглядеть так:
    // average(1), average(1, 2), average(1, 2, 3, 4, 5, 10) и тп.

    private static double average(int... numbers) {
        if (numbers.length == 0) {
            return 0;
        }
        long sum = 0;
        for (int i = 0; i < numbers.length; i++) {
            sum += numbers[i];
        }
        return (double) sum / numbers.length;
    }

    @Test
    public void calculateAverage() {
        Assertions.assertEquals(5d, average(5));
        Assertions.assertEquals(4d, average(3, 5));
        Assertions.assertEquals(4d, average(3, 4, 5));
        Assertions.assertEquals(Integer.MAX_VALUE, average(Integer.MAX_VALUE, Integer.MAX_VALUE));
//        Раскомментировать, эти строчки тоже должны работать.
        Assertions.assertEquals(0, average());
        Assertions.assertEquals(3.5, average(1, 2, 3, 4, 5, 6));
        Assertions.assertEquals(6.5, average(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
    }
}

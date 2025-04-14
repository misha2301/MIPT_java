package edu.phystech.hw2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

// Преобразует строку в верхний регистр
class Capitalizer implements UnaryOperator<String> {
    @Override
    public String apply(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                result.append((char) (c - ('a' - 'A')));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}

// Возвращает большее абсолютное значение из двух чисел
class AbsoluteMaxFinder implements BinaryOperator<Integer> {
    @Override
    public Integer apply(Integer x, Integer y) {
        return Math.max(Math.abs(x), Math.abs(y));
    }
}

// Проверяет, что длина строки больше 5
class LongStringFilter implements Predicate<String> {
    @Override
    public boolean test(String value) {
        return value.length() > 5;
    }
}

// Проверяет, является ли число полным квадратом
class SquareNumberChecker implements Predicate<Integer> {
    @Override
    public boolean test(Integer number) {
        int root = (int) Math.sqrt(number);
        return root * root == number;
    }
}

// Генератор чётных чисел, начиная с указанного
class NextEvenGenerator implements Supplier<Integer> {
    private int next;

    public NextEvenGenerator(int start) {
        this.next = (start % 2 == 0) ? start : start + 1;
    }

    @Override
    public Integer get() {
        int current = next;
        next += 2;
        return current;
    }
}

// Тесты для функциональных интерфейсов
public class FunctionalInterfacesTest {

    @Test
    public void upperCaseOperatorTest() {
        var input = new ArrayList<>(List.of("abC", "edf"));
        input.replaceAll(new Capitalizer());
        Assertions.assertEquals(List.of("ABC", "EDF"), input);
    }

    @Test
    public void maxAbsOperatorTest() {
        var reduced = Stream.of(2, 3, 1, -10).reduce(4, new AbsoluteMaxFinder());
        Assertions.assertEquals(10, reduced);
    }

    @Test
    public void predicateImplementationsTest() {
        var longStrings = Stream.of("a", "bb", "ccc", "1234567", "aaaaaaaaa")
                .filter(new LongStringFilter())
                .toList();
        Assertions.assertEquals(List.of("1234567", "aaaaaaaaa"), longStrings);

        var squares = Stream.of(1, 4, 5, 10, 16, 25)
                .filter(new SquareNumberChecker())
                .toList();
        Assertions.assertEquals(List.of(1, 4, 16, 25), squares);
    }

    @Test
    public void evenNumberSupplierTest() {
        var generator = new NextEvenGenerator(0);
        Stream.of(0, 2, 4, 6, 8, 10).forEach(n -> Assertions.assertEquals(n, generator.get()));

        var another = new NextEvenGenerator(11);
        Stream.of(12, 14, 16, 18, 20, 22).forEach(n -> Assertions.assertEquals(n, another.get()));
    }
}

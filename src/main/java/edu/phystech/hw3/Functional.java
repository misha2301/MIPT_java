package edu.phystech.hw3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class Functional {

    public static <T, R> List<R> map(List<? extends T> collection, Function<T, R> function) {
        List<R> result = new ArrayList<>();
        for (T item : collection) {
            result.add((function.apply(item)));
        }
        return result;
    }

    public static <T, R> T reduce(List<T> collection, BinaryOperator<T> operator, T identity) {
        T result = identity;
        for (T item : collection) {
            result = operator.apply(result, item);
        }
        return result;
    }
}





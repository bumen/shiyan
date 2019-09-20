package com.bmn.rt.java8;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class LambdaApi {


    private Consumer<Object> consumer;
    private Predicate<Object> predicate;
    private Function<Object, Object> function;
    private Supplier<Object> supplier;
    private UnaryOperator<Object> unaryOperator;
    private BinaryOperator<Object> binaryOperator;


    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);

        boolean match = list.stream().anyMatch(i -> i == 3);

        System.out.println(match);
    }
}

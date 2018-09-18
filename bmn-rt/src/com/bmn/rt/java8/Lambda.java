package com.bmn.rt.java8;

import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class Lambda {


    private Consumer<Object> consumer;
    private Predicate<Object> predicate;
    private Function<Object, Object> function;
    private Supplier<Object> supplier;
    private UnaryOperator<Object> unaryOperator;
    private BinaryOperator<Object> binaryOperator;
}

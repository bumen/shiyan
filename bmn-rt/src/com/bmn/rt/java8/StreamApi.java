package com.bmn.rt.java8;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: zyq
 * @date: 2018/11/16
 *
 *
 *
 */
public class StreamApi {

    public static void main(String[] args) {
        mapAndFlatMapTest();
    }

    public static void mapAndFlatMapTest() {
        List<String> data = Arrays.asList("习近平重要讲话", "11111", "22222", "11111");

        //data.stream().distinct().map(word -> word + "-maped").collect(Collectors.toList()).forEach(System.out::println);

        data.stream().distinct().flatMap(word -> Arrays.stream(data.toArray())).collect(Collectors.toList()).forEach(System.out::println);
    }
}

package com.bmn.rt.java8;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

/**
 * @author: zyq
 * @date: 2018/11/16
 */
public class StreamApi {

    public static void main(String[] args) {
        mapAndFlatMapTest();
    }

    public static void mapAndFlatMapTest() {
        List<String> data = asList("习近平重要讲话", "11111", "22222", "11111");

        data.stream().distinct().map(word -> word + "-maped").collect(Collectors.toList()).forEach(System.out::println);

        List<Object> v = data.stream().distinct().flatMap(word ->
            Arrays.stream(data.toArray()))
            .collect(Collectors.toList());

        System.out.println(v);

        List<Integer> vv2 = Stream.of(asList(1, 3), asList(2, 4)).flatMap(n -> n.stream()).collect(Collectors.toList());
        System.out.println(vv2);
    }
}

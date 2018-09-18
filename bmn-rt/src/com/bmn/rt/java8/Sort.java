package com.bmn.rt.java8;

import com.bmn.rt.beans.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Stream;

public class Sort {

    private Spliterator<Object> spliterator;
    private Stream<Object> stream;

    private static List<User> users = new ArrayList<>();

    public static void init() {
        users.add(new User("jack", 17, 10));
        users.add(new User("jack", 18, 10));
        users.add(new User("jack", 19, 11));
        users.add(new User("apple", 25, 15));
        users.add(new User("tommy", 23, 8));
        users.add(new User("jessica", 15, 13));



    }

    private static  Comparator<User> c = Comparator.comparing(User::getName).thenComparing(User::getAge, (x,y)-> x - y > 0 ? -1: 1).thenComparing(User::getCredits);

    public static void sort() {
        users.sort(c);
    }

    public static void main(String[] args) {
        Sort.init();
        Sort.sort();

        System.out.println(users);
    }

}

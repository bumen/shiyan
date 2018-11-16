package com.bmn.rt.java8;

import com.bmn.rt.beans.User;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Stream;

public class SortApi {

    private Spliterator<Object> spliterator;
    private Stream<Object> stream;

    private static List<User> users = new ArrayList<>();

    public static void init() {
        users.add(new User("jack", 17, 10, 4));
        users.add(new User("jack", 17, 12, 3));
        users.add(new User("jack", 19, 11, 3));
        users.add(new User("jack", 17, 12, 1));
        users.add(new User("apple", 25, 15, 1));
        users.add(new User("tommy", 23, 8, 1));
        users.add(new User("jessica", 15, 13, 1));


    }

    /**
     * 多条件
     */
    private static Comparator<User> c = Comparator.comparing(User::getAge)
        .thenComparing(User::getCredits, (x, y) -> {
            return x == y ? 0 : x > y ? 1 : -1;
        })
        .thenComparing(User::getImage, (x, y) -> {
            return x == y ? 0 : x > y ? 1 : -1;
        });

    public static void sort() {
        users.sort(c);

        // Collections.sort(users, c);
    }

    public static void main(String[] args) {
        SortApi.init();
        SortApi.sort();

        System.out.println(users);
    }

}

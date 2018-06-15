package com.bmn.rt.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/21.
 */
public class ListTest {

    public static void main(String[] args) {
        List<Integer>  list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        iteratorTest(list, 3);

        List<String> l = new LinkedList<>();
    }
    private static <E> void iteratorTest(List<E> list, E removed) {
        System.out.println("iterator test start");
        Iterator<E> it = list.iterator();
        while(it.hasNext()) {
            E i = it.next();
            System.out.println(i);

            if(i == removed) {
                it.remove();
            }
        }
        System.out.println("iterator test end");
    }
}

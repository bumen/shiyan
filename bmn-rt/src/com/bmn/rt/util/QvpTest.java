package com.bmn.rt.util;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Administrator on 2017/9/21.
 */
public class QvpTest {

    public static void main(String[] args) {
        Integer[] data = {1,2,3,4,5};
        QvpList<Integer> list = new QvpList<>(data);

        iteratorTest(list, 3);

        list = new QvpList<>(data);
        listIteratorTest(list, 3, 11);
    }

    public static <E> void iteratorTest(List<E> list, E del) {
        System.out.println("start");
        Iterator<E> it = list.iterator();
        while(it.hasNext()) {
            E i = it.next();
            System.out.println(i);

            if(i == del) {
                it.remove();
                //it.remove();
            }
        }
        System.out.println("end");
    }

    public static <E> void listIteratorTest(List<E> list, E del, E add) {
        System.out.println("start");
        ListIterator<E> it = list.listIterator(list.size());
        while(it.hasPrevious()) {
            E i = it.previous();
            System.out.println(i);

            if(i == del) {
                it.remove();
            }
            it.set(add);

        }
        System.out.println("end");
    }
}

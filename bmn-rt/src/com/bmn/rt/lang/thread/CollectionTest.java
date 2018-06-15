package com.bmn.rt.lang.thread;

import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/11/21.
 */
public class CollectionTest {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();


    class MyThreadLocal extends ThreadLocal<String> {
        @Override
        protected String initialValue() {
            return "hello";
        }
    }

    public static void main(String[] args) {


        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();

        CopyOnWriteArraySet<Integer> set = new CopyOnWriteArraySet<>();

        ConcurrentHashMap hashMap = new ConcurrentHashMap();

        ConcurrentSkipListMap<Integer, Integer> skipListMap = new ConcurrentSkipListMap<>();

        ConcurrentSkipListSet<Integer> skipListSet = new ConcurrentSkipListSet<>();

        ArrayBlockingQueue<Integer> arrayBlockingQueue = new ArrayBlockingQueue<Integer>(1);

        LinkedBlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>();

        LinkedBlockingDeque<Integer> linkedBlockingDeque = new LinkedBlockingDeque<>();

        ConcurrentLinkedQueue<Integer> linkedQueue = new ConcurrentLinkedQueue<>();



    }
}

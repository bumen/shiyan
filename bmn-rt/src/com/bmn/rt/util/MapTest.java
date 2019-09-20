package com.bmn.rt.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Administrator on 2017/9/29.
 */
public class MapTest {

    public static void main(String[] args) throws InterruptedException {
//        threads();
        putNull();
    }

    private static void putNull() {
        Map<Integer, Integer> data = new HashMap<>(10);

        Integer v = null;
        data.put(1, 3 + v);
    }

    private static void threads() throws InterruptedException {

        Map<Integer, Integer> data = new HashMap<>(10);

        CountDownLatch latch = new CountDownLatch(20);

        CountDownLatch latch1 = new CountDownLatch(1);

        for (int i = 0; i < 11; i++) {
            int idx = i;
            Thread t =  new Thread(()->{
                latch.countDown();

                try {
                    latch1.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (int j = 0; j < 50; j++) {
                    data.put(idx, idx);
                }

            });
            t.start();

            Thread consumer = new Thread(() -> {
                latch.countDown();

                try {
                    latch1.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (int j = 0; j < 12; j++) {
//                    for (Integer x : data.keySet()) {
//                        System.out.println(x);
//                    }
                    data.clear();
//                    System.out.println(x);
                }
            });
            consumer.start();
        }

        latch.await();

        // 同时执行
        latch1.countDown();


        Thread.sleep(5000);

        System.out.println(data.size());
    }


    private static void basic() {
        Hashtable<Integer, Integer> table = new Hashtable<>(2);
        //table.put(3, null);
        table.put(1, 1);
        table.put(2, 2);
        table.put(3, 3);


        LinkedHashMap<Integer, Integer> linkedHashMap = new LinkedHashMap<>(3, 0.75f, true);

        linkedHashMap.put(3, 3);

        linkedHashMap.put(2, 2);
        linkedHashMap.put(1, 1);

        linkedHashMap.get(3);
        linkedHashMap.get(1);

        linkedHashMap.remove(3);

        HashMap<String, String> map = new HashMap<>(3);
        map.put("1", "1");
        map.put("2", "2");
        map.put("3", "2");
        map.put("4", "2");
        map.put("5", "2");
        map.put("6", "2");
        map.put("7", "2");
        map.put("8", "2");
        map.put("9", "2");
        map.put("10", "2");
        map.put("11", "2");
        map.put("12", "2");
        map.put("13", "2");


        HashSet<Integer> set = new HashSet<>();


        TreeMap<Integer, Integer> treeMap = new TreeMap<>();

        int hash = mapHash("1");

        int oldCap = 16;

        int index = hash & (oldCap - 1);
        int sp = 17 & oldCap;

        System.out.println("hash " + hash + " index : " + index + " sp " + sp);

        WeakHashMap<Integer, Integer> weekHashMap = new WeakHashMap<>();
    }

    private static int mapHash(String key) {
        int h;
        return key == null ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}

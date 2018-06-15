package com.bmn.rt.util;

import java.util.*;

/**
 * Created by Administrator on 2017/9/29.
 */
public class MapTest {

    public static void main(String[] args) {


        Hashtable<Integer, Integer> table = new Hashtable<>(2);
        table.put(3, null);
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

        HashMap<String, String> map = new HashMap<>();
        map.put("1", "1");
        map.put("2", "2");


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

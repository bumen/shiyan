package com.bmn.rt.util;

import sun.util.locale.BaseLocale;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;

/**
 * Created by Administrator on 2017/10/9.
 */
public class PriorityQueueTest {
    public static void main(String[] args) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(5);
        priorityQueue.add(4);
        priorityQueue.add(32);
        priorityQueue.add(41);
        priorityQueue.add(22);

        System.out.println(priorityQueue.peek());

        for(int i = 0, l = priorityQueue.size(); i < l; i++) {
            System.out.println(priorityQueue.poll());
        }

        System.out.println(ClassLoader.getSystemClassLoader());

        System.out.println(Locale.CHINA.getCountry());
        System.out.println(Locale.CHINA.getLanguage());
        System.out.println(Locale.CHINA.getScript());
        System.out.println(Locale.CHINA.getVariant());

        Locale locale = new Locale("zh", "cn", "asdasdf");

        System.out.println(locale.getCountry());
        System.out.println(locale.getLanguage());
        System.out.println(locale.getScript());
        System.out.println(locale.getVariant());

        variant("asdfasdf_1_2_3_4_");
    }


    public static void variant(String variant) {
        List<String> variants = null;

        if (variant.length() > 0) {
            variants = new LinkedList<>();
            int idx = variant.length();
            while (idx != -1) {
                variants.add(variant.substring(0, idx));
                idx = variant.lastIndexOf('_', --idx);
            }
        }

        for(String s : variants) {
            System.out.println(s);
        }
    }
}

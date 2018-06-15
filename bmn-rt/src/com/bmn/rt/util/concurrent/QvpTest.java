package com.bmn.rt.util.concurrent;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Administrator on 2017/11/14.
 */
public class QvpTest {
    public static void main(String[] args) {
        testConcurrentLinkedQueue();
    }

    private static void testConcurrentLinkedQueue() {
        QvpBlockingQueue<String> queue = new QvpBlockingQueue<>();
        queue.offer("zahang");
        queue.poll();

        ConcurrentLinkedQueue<String> queue2 = new ConcurrentLinkedQueue<>();
        queue2.offer("1");
       // queue2.offer("2");
        queue2.poll();
        queue2.poll();

        String t = "3";



        System.out.println((t != (t = "5")));

        System.out.println(t);
    }
}

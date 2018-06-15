package com.bmn.rt.util;

import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by Administrator on 2017/4/20.
 */
public class ExecutorsTest {
    public static void main(String[] args) {
        int COUNT_BITS = Integer.SIZE - 3;
        int a =  -1 << COUNT_BITS;
         a += 5;
        System.out.println(a < 0);
        System.out.println(Integer.toBinaryString(a - 1));

        System.out.println("01100000000000000000000000000000".length());
        Executors executors = null;

        final QvpSynchronousQueue<Integer> queue = new QvpSynchronousQueue<>(false);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    System.out.println("t1 putting...");
                    queue.put(1);
                    System.out.println("t1 ended");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    System.out.println("t2 putting...");
                    queue.put(2);
                    System.out.println("t2 ended");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t2.start();

        Thread p1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(6000);
                    System.out.println("start take thread...");
                    int a =  queue.take();
                    System.out.println("take thread t" + a + " end");

                    System.out.println("start take thread...");
                    a = queue.take();
                    System.out.println("take thread t" + a + " end");

                    System.out.println("start take thread...");
                    a = queue.take();
                    System.out.println("take thread t" + a + " end");

                    System.out.println("start take thread...");
                    a = queue.take();
                    System.out.println("take thread t" + a + " end");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        p1.start();


        try {
            System.out.println("t0 putting...");
            queue.put(0);
            System.out.println("t0 ended");

            System.out.println("t3 putting...");
            queue.put(3);
            System.out.println("t3 ended");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }
}

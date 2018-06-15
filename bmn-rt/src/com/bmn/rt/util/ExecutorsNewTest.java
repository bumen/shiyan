package com.bmn.rt.util;

import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/4/20.
 */
public class ExecutorsNewTest {
    public static void main(String[] args) {
        Executor executor = new QvpThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());


        for(int i = 0; i < 100; i++) {
            addRunnable(executor);
        }



        /** Return true if m has fulfilling bit set */
        System.out.println(isFulfilling(3));

    }

    static final int FULFILLING = 2;

    static boolean isFulfilling(int m) { return (m & FULFILLING) != 0; }

    private static void addRunnable(Executor executor) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName());
            }
        });
    }


}

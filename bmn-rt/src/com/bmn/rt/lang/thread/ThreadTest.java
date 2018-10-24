package com.bmn.rt.lang.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

/**
 * Created by Administrator on 2017/11/17.
 */
public class ThreadTest {

    public static void main(String[] args) {


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.yield();

            }
        });

        Thread.currentThread().getContextClassLoader();


        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();


        AtomicLong atomicLong;
        AtomicLongArray atomicLongArray;

        ThreadLocal threadLocal ;
    }

}

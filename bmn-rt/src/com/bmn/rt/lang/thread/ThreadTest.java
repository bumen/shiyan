package com.bmn.rt.lang.thread;

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


        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        AtomicLong atomicLong;
        AtomicLongArray atomicLongArray;
    }

}

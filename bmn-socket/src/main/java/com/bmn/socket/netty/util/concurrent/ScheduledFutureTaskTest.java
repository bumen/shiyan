package com.bmn.socket.netty.util.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/3/7.
 */
public class ScheduledFutureTaskTest {

    public static void main(String[] args) throws InterruptedException {
        ScheduledFutureTask task = new ScheduledFutureTask(null, new Runnable() {
            @Override
            public void run() {
                System.out.println("ahaha my runging");
            }
        }, "hasdf", 1000000000);





        long S_T = System.nanoTime();

        Thread.sleep(500);
        long E_T = System.nanoTime();

        System.out.println(E_T - S_T);

        long v = task.getDelay(TimeUnit.MILLISECONDS);
        System.out.println("V " +v);
    }
}

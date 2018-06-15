package com.bmn.rt.lang.thread.pool;

import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/8/22.
 */
public class ThreadPollExecutorTest {


    public static void main(String[] args) {
      ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 10,
                3000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());

        executor = new ThreadPoolExecutor(0, 2,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());

        executor.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("ahahahah : " + Thread.currentThread().isInterrupted());
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.execute(new Runnable() {
           @Override
           public void run() {
               try {
                   Thread.sleep(60000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               System.out.println("second ");
           }
       });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("thied");
            }
        });


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdownNow();

        System.out.println("shutdown now");

    }
}

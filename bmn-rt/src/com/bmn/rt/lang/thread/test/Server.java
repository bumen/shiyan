package com.bmn.rt.lang.thread.test;

/**
 * Created by Administrator on 2017/11/17.
 */
public class Server {

    private Service appService;
    private Service dbService;


    public Server() {
        appService = new AppService();
        dbService = new DbService();
    }

    public void start() {
        final Thread t1  = new Thread(new Runnable() {
            @Override
            public void run() {
                appService.start();
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (appService) {
                    System.out.println(Thread.currentThread().getName()+" start t1");
                    t1.start();
                        System.out.println(Thread.currentThread().getName()+" wait()");
                   /* try {
                        //this.wait();
                    } catch (InterruptedException e) {
                       // e.printStackTrace();
                       // Thread.currentThread().interrupt();
                        System.out.println(Thread.currentThread().getName() + " interrupted ");
                    }*/
                    System.out.println(Thread.currentThread().getName()+" continue " + Thread.currentThread().isInterrupted());
                }
            }
        });

        t2.start();

        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t2.interrupt();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(t2.getName()+" finally " + t2.isInterrupted());
    }

}

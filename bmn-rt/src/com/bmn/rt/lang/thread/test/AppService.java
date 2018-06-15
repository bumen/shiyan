package com.bmn.rt.lang.thread.test;

/**
 * Created by Administrator on 2017/11/17.
 */
public class AppService implements Service{

    private String name = "qvp";

    @Override
    public void start() {

        synchronized (this) {
            System.out.println(Thread.currentThread().getName() + " call notify()");
            // 唤醒当前的wait线程
            this.notify();

            Thread.yield();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
            try {
                Thread.currentThread().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void service() {
        synchronized (this) {

        }
    }
}

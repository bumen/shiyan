package com.bmn.rt.util;

/**
 * Created by Administrator on 2017/4/20.
 */
public class SynchronousQueueTest {
    public static void main(String[] args) throws InterruptedException {
        final QvpSynchronousQueue<Integer> queue = new QvpSynchronousQueue<>();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    queue.offer(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();

        queue.offer(3);


    }
}

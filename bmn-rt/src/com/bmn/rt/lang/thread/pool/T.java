package com.bmn.rt.lang.thread.pool;

import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/11/30.
 */
public class T {

    private static void st() {
        final SynchronousQueue<Integer> queue1 = new SynchronousQueue<>(true);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    queue1.put(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("running1");

            }
        });
        t.start();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("running2");
                queue1.poll();
                System.out.println("running2");

            }
        });
        t1.start();
    }

    public static void main(String[] args) throws InterruptedException {
        st();
        final SynchronousQueue<Integer> queue1 = new SynchronousQueue<>(true);


        queue1.put(3);

        queue1.offer(3);

        Executors executors;


        final CountDownLatch lath = new CountDownLatch(1);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                queue1.poll();
                System.out.println("running");

                lath.countDown();
            }
        });

        System.out.println(t.isAlive());

        t.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("t : " + t.isAlive());

        try {
            lath.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("end " + t.isAlive());

        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();

        Thread tt = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    synchronized (queue) {
                        System.out.println("tt had lock");
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    System.out.println("tt end lock");

                    System.out.println("before interrupt");
                    Thread.currentThread().interrupt();
                    System.out.println("after i nterrupt");
                } finally {
                    System.out.println("finally ");
                }

            }
        });

        tt.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("start ttt...");
        Thread ttt = new Thread(new Runnable() {
            @Override
            public void run() {

                synchronized (queue) {
                    System.out.println("ttt had lock :" + Thread.currentThread().isInterrupted());
                }
                System.out.println("ttt end lock");
            }
        });
        ttt.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        ttt.interrupt();
        System.out.println("ttt interrupted");
    }
}

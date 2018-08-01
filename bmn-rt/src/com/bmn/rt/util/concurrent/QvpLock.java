package com.bmn.rt.util.concurrent;

import sun.misc.Unsafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Administrator on 2017/11/28.
 */
public class QvpLock {
    private volatile  int count;

    private volatile  long longCount;

    private volatile String name;

    public static void main(String[] args) {
        AtomicLong atomicLong = new AtomicLong();
        AtomicInteger atomicInteger = new AtomicInteger();

        atomicInteger.getAndAdd(10);
        atomicInteger.lazySet(3);

        System.out.println(atomicInteger.get());


        AtomicBoolean atomicBoolean = new AtomicBoolean();

        AtomicReference atomicReference = new AtomicReference();

        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(3);

        AtomicReferenceArray atomicReferenceArray = new AtomicReferenceArray(3);

        AtomicIntegerFieldUpdater atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(QvpLock.class, "count");

        AtomicLongFieldUpdater.newUpdater(QvpLock.class, "longCount");

        AtomicReferenceFieldUpdater.newUpdater(QvpLock.class, String.class, "name");


        System.out.println(Unsafe.ARRAY_INT_BASE_OFFSET);


       // semaphores();
        //locks();

        countDownLatch();
    }



    private static void locks() {
       final ReentrantLock lock = new ReentrantLock();

        final Condition condition = lock.newCondition();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                lock.lock();

                condition.signalAll();

                lock.unlock();
            }
        }).start();

        lock.lock();
        try {
            condition.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }

        if(lock.tryLock()) {
            try {
                condition.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }


        try {
            lock.lockInterruptibly();
            try {
                condition.await();
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        condition.signal();


        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

        AbstractQueuedSynchronizer abstractQueuedSynchronizer;



    }

    private static void semaphores() {
        Semaphore semaphore = new Semaphore(2);

        semaphore.release(10);

        System.out.println("semaphore: " + semaphore.availablePermits());


        CountDownLatch countDownLatch = new CountDownLatch(1);
    }

    public static void countDownLatch() {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
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
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

       // t2.start();

        countDownLatch.countDown();
        //countDownLatch.countDown();
    }

    public static void readWriteLock() {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    }
}

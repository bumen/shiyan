package com.bmn.rt.util.concurrent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;

/**
 * @author: zyq
 * @date: 2018/11/16
 */
public class TestUtils {


    public static void main(String[] args) throws InterruptedException {
        map.put("123", new QvpUnsafeInteger());
        testConcurrentHashMap();
    }

    private static final ThreadFactory threadFactory = new QvpThreadFactory();

    /**
     * 新插入的key，则返回null, 当再插入相同key时, 返回旧value
     */
    private static final ConcurrentHashMap<String, QvpUnsafeInteger> map = new ConcurrentHashMap<>();

    /**
     *
     * @throws InterruptedException
     */
    private static void testConcurrentHashMap() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            threadFactory.newThread(() -> {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //map.put("123", Thread.currentThread().getName());
                int j = 100;
                while (j > 0) {
                    QvpUnsafeInteger in = map.get("123");
                    // 锁局部变量，但是成员变量引用，是线程安全的。
                    synchronized (in) {
                        in.increase();
                    }
                    j--;
                }

                end.countDown();
                //
            }).start();
        }

        latch.countDown();

        end.await();

        System.out.println(map.get("123").getValue());
    }

}

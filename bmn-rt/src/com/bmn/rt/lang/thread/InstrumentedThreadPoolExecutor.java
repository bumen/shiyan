package com.bmn.rt.lang.thread;

import sun.util.resources.cldr.ga.LocaleNames_ga;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Administrator on 2017/8/16.
 */
public class InstrumentedThreadPoolExecutor extends ThreadPoolExecutor {

    public InstrumentedThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
                                          long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

// Keep track of all of the request times
    private final ConcurrentHashMap<Runnable, Long> timeOfRequest =     //每个请求进行进入线程池时间
            new ConcurrentHashMap<>();
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();    //某线程开始执行时间
    private long lastArrivalTime;   //上一个请求，的发起时间


    // other variables are AtomicLongs and AtomicIntegers
    private AtomicLong totalServiceTime = new AtomicLong();     //所有线程的执行时间， 从开始执行到执行结束
    private AtomicLong totalPoolTime = new AtomicLong();        //所有请求在线程池内等待执行的时间，从进入到执行
    private AtomicInteger numberOfRequestsRetired = new AtomicInteger();    //所有执行完的请求数
    private AtomicInteger numberOfRequests = new AtomicInteger();           //所有请求数
    private AtomicLong aggregateInterRequestArrivalTime = new AtomicLong(); //所有请求的，请求间隔时间和


    @Override
    protected void beforeExecute(Thread worker, Runnable task) {
        super.beforeExecute(worker, task);
        startTime.set(System.nanoTime());
    }

    @Override
    protected void afterExecute(Runnable task, Throwable t) {
        try {
            totalServiceTime.addAndGet(System.nanoTime() - startTime.get());
            totalPoolTime.addAndGet(startTime.get() - timeOfRequest.remove(task));
            numberOfRequestsRetired.incrementAndGet();
        } finally {
            super.afterExecute(task, t);
        }
    }

    @Override
    public void execute(Runnable task) {
        long now = System.nanoTime();

        numberOfRequests.incrementAndGet();
        synchronized (this) {
            if (lastArrivalTime != 0L) {
                aggregateInterRequestArrivalTime.addAndGet(now - lastArrivalTime);
            }
            lastArrivalTime = now;
            timeOfRequest.put(task, now);
        }
        super.execute(task);
    }

    public long getAggregateInterRequestArrivalTime() {
        return this.aggregateInterRequestArrivalTime.get();
    }

    public long getTotalServiceTime() {
        return totalServiceTime.get();
    }

    public long getTotalPoolTime() {
        return totalPoolTime.get();
    }

    public int getNumberOfRequestsRetired() {
        return numberOfRequestsRetired.get();
    }
}
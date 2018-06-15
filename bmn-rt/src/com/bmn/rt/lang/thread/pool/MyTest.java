package com.bmn.rt.lang.thread.pool;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2017/12/1.
 */
public class MyTest extends PoolSizeCalculator {

    public static void main(String[] args) throws InterruptedException,
            InstantiationException,
            IllegalAccessException,
            ClassNotFoundException {
        MyTest calculator = new MyTest();
        calculator.calculateBoundaries(new BigDecimal(1.0),
                new BigDecimal(100000));
    }

    protected long getCurrentThreadCPUTime() {
        return ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
    }

    protected Runnable creatTask() {
        return new AsyncTask(AsyncTask.CPU);
    }

    protected BlockingQueue<Runnable> createWorkQueue() {
        return new LinkedBlockingQueue<>();
    }
}

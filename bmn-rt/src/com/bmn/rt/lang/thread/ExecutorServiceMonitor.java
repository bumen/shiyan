package com.bmn.rt.lang.thread;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/8/16.
 */
public class ExecutorServiceMonitor
        implements ExecutorServiceMonitorMXBean {


    private InstrumentedThreadPoolExecutor threadPool;


    /**
     * 请求的退出率或观测率
     * @return
     */
    public double getRequestPerSecondRetirementRate() {
        return (double) getNumberOfRequestsRetired() /
                fromNanoToSeconds(threadPool.getAggregateInterRequestArrivalTime());
    }

    public double getAverageServiceTime() {
        return fromNanoToSeconds(threadPool.getTotalServiceTime()) /
                (double)getNumberOfRequestsRetired();
    }

    public double getAverageTimeWaitingInPool() {
        return fromNanoToSeconds(this.threadPool.getTotalPoolTime()) /
                (double) this.getNumberOfRequestsRetired();
    }

    private double fromNanoToSeconds(long totalPoolTime) {
        return TimeUnit.NANOSECONDS.toSeconds(totalPoolTime);
    }

    private int getNumberOfRequestsRetired() {
        return threadPool.getNumberOfRequestsRetired();
    }

    /**
     * 多个请求的，平均响应时间
     * @return
     */
    public double getAverageResponseTime() {
        return this.getAverageServiceTime() +
                this.getAverageTimeWaitingInPool();
    }


    /**
     * 利特尔法则    一个系统请求数=请求的退出率或观测率 * 平均每个单独请求花费的时间。
     * @return
     */
    public double getEstimatedAverageNumberOfActiveRequests() {
        return getRequestPerSecondRetirementRate() * (getAverageServiceTime() +
                getAverageTimeWaitingInPool());
    }

    public double getRatioOfDeadTimeToResponseTime() {
        double poolTime = (double) this.threadPool.getTotalPoolTime();
        return poolTime /
                (poolTime + (double)threadPool.getTotalServiceTime());
    }

    public double v() {
        return getEstimatedAverageNumberOfActiveRequests() /
                (double) Runtime.getRuntime().availableProcessors();
    }
}

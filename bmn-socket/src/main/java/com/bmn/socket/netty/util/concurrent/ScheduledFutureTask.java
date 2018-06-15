package com.bmn.socket.netty.util.concurrent;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017/1/12.
 */
public class ScheduledFutureTask<V> extends PromiseTask<V> implements ScheduledFuture<V> {
    private static final AtomicInteger nextTaskId = new AtomicInteger();
    private static final long START_TIME = System.nanoTime();

    static long nanoTime() {
        return System.nanoTime() - START_TIME;
    }

    static long deadlineNanos(long delay) {
        return nanoTime() + delay;
    }

    private final long id = nextTaskId.getAndIncrement();
    private long deadlineNanos;
    private final long periodNanos;

    ScheduledFutureTask(AbstractScheduledEventExecutor executor, Runnable runnable, V result, long nanoTime) {
        this(executor, toCallable(runnable, result), nanoTime);
    }

    ScheduledFutureTask(AbstractScheduledEventExecutor executor, Callable<V> callable, long nanoTime, long period) {
        super(executor, callable);
        deadlineNanos = nanoTime;
        periodNanos = period;
    }

    ScheduledFutureTask(AbstractScheduledEventExecutor executor, Callable<V> callable, long nanoTime) {
        super(executor, callable);
        deadlineNanos = nanoTime;
        periodNanos = 0;
    }

    @Override
    protected EventExecutor executor() {
        return super.executor();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(delayNanos(), TimeUnit.NANOSECONDS);
    }

    public long deadlineNanos() {
        return this.deadlineNanos;
    }

    public long delayNanos() {
        return Math.max(0, deadlineNanos() - nanoTime());
    }

    public long delayNanos(long currentTimeNanos) {
        return Math.max(0, deadlineNanos() - (currentTimeNanos - START_TIME));
    }



    @Override
    public int compareTo(Delayed o) {
        if(this == o)
            return 0;
        ScheduledFutureTask<?> that = (ScheduledFutureTask)o;
        long d = deadlineNanos() - that.deadlineNanos();
        if(d < 0) {
            return -1;
        } else if(d > 0) {
            return 1;
        } else if( id < that.id) {
            return -1;
        } else if(id == that.id) {
            throw new Error();
        } else {
            return 1;
        }
    }

    public void run() {
        try {
            if(periodNanos == 0) {
                if(setUncancellableInternal()) {
                    V result = task.call();
                    setSuccessInternal(result);
                }
            } else {
                if(!isCancelled()) {
                    task.call();
                    if(!executor().isShutdown()) {
                        long p = periodNanos;
                        if( p > 0) {
                            deadlineNanos += p;
                        } else {
                            deadlineNanos = nanoTime() - p;
                        }

                        if(!isCancelled()) {
                            Queue<ScheduledFutureTask<?>> scheduledTaskQueue = ((AbstractScheduledEventExecutor)executor()).scheduledTaskQueue;
                            scheduledTaskQueue.add(this);
                        }
                    }
                }
            }

        } catch (Throwable e) {
            setFailureInternal(e);
        }
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        boolean canceled = super.cancel(mayInterruptIfRunning);
        if(canceled) {
            ((AbstractScheduledEventExecutor)executor()).removeScheduled(this);
        }
        return canceled;
    }

    boolean cancelWithoutRemove(boolean mayInterruptIfRunning) {
        return super.cancel(mayInterruptIfRunning);
    }


}

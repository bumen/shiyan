package com.bmn.socket.netty.util.concurrent;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/1/6.
 */
public abstract class AbstractEventExecutor extends AbstractExecutorService implements EventExecutor {
    static final long DEFAULT_SHUTDOWN_QUIET_PERIOD = 2L;
    static final long DEFAULT_SHUTDOWN_TIMEOUT = 15L;
    private final EventExecutorGroup parent;
    private final Collection<EventExecutor> selfCollection = Collections.<EventExecutor>singleton(this);

    protected AbstractEventExecutor() {this(null);}

    protected AbstractEventExecutor(EventExecutorGroup parent) {
        this.parent = parent;
    }

    public EventExecutorGroup parent() {return parent;}

    public EventExecutor next() {return this;}

    public boolean inEventLoop() {
        return this.inEventLoop(Thread.currentThread());
    }

    public Iterator<EventExecutor> iterator() {
        return this.selfCollection.iterator();
    }

    public java.util.concurrent.Future<?> shutdownGracefully() {
        return this.shutdownGracefully(DEFAULT_SHUTDOWN_QUIET_PERIOD, DEFAULT_SHUTDOWN_TIMEOUT, TimeUnit.SECONDS);
    }

    public <V> Promise<V> newPromise() {return new DefaultPromise<>(this);}

    public <V> Future<V> newSucceededFuture(V result) {
        return new SucceededFuture<>(this, result);
    }

    public <V> Future<V> newFailedFuture(Throwable e) {
        return new FailedFuture<>(this, e);
    }

    public Future<?> submit(Runnable task){return (Future)super.submit(task);}


    public <T> Future<T> submit(Runnable task, T result){return (Future)super.submit(task, result);}

    public <T> Future<T> submit(Callable<T> task) {return (Future)super.submit(task);}

    protected final <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return new PromiseTask(this, runnable, value);
    }

    protected final <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new PromiseTask(this, callable);
    }

    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        throw new UnsupportedOperationException();
    }

    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        throw new UnsupportedOperationException();
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        throw new UnsupportedOperationException();
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        throw new UnsupportedOperationException();
    }


}

package com.bmn.socket.netty.util.concurrent;

import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/1/6.
 */
public interface EventExecutorGroup extends ScheduledExecutorService, Iterable<EventExecutor>{
    boolean isShuttingDown();

    java.util.concurrent.Future<?> shutdownGracefully();

    java.util.concurrent.Future<?> shutdownGracefully(long var1, long var3, TimeUnit var5);

    Future<?> terminationFuture();

    EventExecutor next();

    Iterator<EventExecutor> iterator();

    Future<?> submit(Runnable var1);

    <T> Future<T> submit(Runnable var1, T var3);

    <T> Future<T> submit(Callable<T> var1);

    ScheduledFuture<?> schedule(Runnable var1, long var2, TimeUnit var4);

    <V> ScheduledFuture<V> schedule(Callable<V> var1, long var2, TimeUnit var4);

    @Override
    ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit);

    @Override
    ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit);
}

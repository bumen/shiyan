package com.bmn.socket.netty.util.concurrent;

/**
 * Created by Administrator on 2017/1/6.
 */
public interface EventExecutor extends EventExecutorGroup{
    EventExecutor next();

    EventExecutorGroup parent();

    boolean inEventLoop();

    boolean inEventLoop(Thread var1);

    <V> Promise<V> newPromise();

    <V> java.util.concurrent.Future<V> newSuccessedFuture(V var1);

    <V> Future<V> newFailedFuture(Throwable var1);
}

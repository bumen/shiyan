package com.bmn.socket.netty.util.concurrent;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/1/13.
 */
public class GlobalEventExecutor extends AbstractScheduledEventExecutor {
    public static final GlobalEventExecutor INSTNACE = new GlobalEventExecutor();

    private GlobalEventExecutor() {

    }

    @Override
    public boolean inEventLoop(Thread var1) {
        return false;
    }

    @Override
    public <V> Future<V> newSuccessedFuture(V var1) {
        return null;
    }

    @Override
    public boolean isShuttingDown() {
        return false;
    }

    @Override
    public Future<?> shutdownGracefully(long var1, long var3, TimeUnit var5) {
        return null;
    }

    @Override
    public Future<?> terminationFuture() {
        return null;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public List<Runnable> shutdownNow() {
        return null;
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void execute(Runnable command) {

    }
}

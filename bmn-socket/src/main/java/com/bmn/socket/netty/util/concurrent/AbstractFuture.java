package com.bmn.socket.netty.util.concurrent;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Administrator on 2017/1/5.
 */
public abstract class AbstractFuture<V> implements Future<V>{
    public AbstractFuture() {
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        this.await();
        Throwable cause = this.cause();
        if(cause == null) {
            return this.getNow();
        } else if(cause instanceof CancellationException) {
            throw (CancellationException) cause;
        } else {
            throw new ExecutionException(cause);
        }
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (this.await(timeout, unit)) {
            Throwable cause = this.cause();
            if(cause == null) {
                return this.getNow();
            } else if(cause instanceof CancellationException) {
                throw (CancellationException) cause;
            } else {
                throw new ExecutionException(cause);
            }
        } else {
            throw new TimeoutException();
        }
    }

}

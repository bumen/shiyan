package com.bmn.socket.netty.util.concurrent;

/**
 * Created by Administrator on 2017/2/27.
 */
public interface ProgressivePromise<V> extends Promise<V>, ProgressiveFuture<V>{

    ProgressivePromise<V> setProgress(long progress, long total);
    boolean tryProgress(long progress, long total);

    @Override
    ProgressivePromise<V> setSuccess(V result);

    @Override
    ProgressivePromise<V> setFailure(Throwable cause);

    @Override
    ProgressivePromise<V> addListener(GenericFutureListener<? extends Future<? super V>> var1);

    @Override
    ProgressivePromise<V> addListeners(GenericFutureListener... var1);

    @Override
    ProgressivePromise<V> removeListener(GenericFutureListener<? extends Future<? super V>> var1);

    @Override
    ProgressivePromise<V> removeListeners(GenericFutureListener... var1);

    @Override
    ProgressivePromise<V> await() throws InterruptedException;

    @Override
    ProgressivePromise<V> awaitUninterruptibly();

    @Override
    ProgressivePromise<V> sync() throws InterruptedException;

    @Override
    ProgressivePromise<V> syncUninterruptibly();
}

package com.bmn.socket.netty.util.concurrent;

/**
 * Created by Administrator on 2017/2/27.
 */
public interface ProgressiveFuture<V> extends Future<V> {

    @Override
    ProgressiveFuture<V> addListener(GenericFutureListener<? extends Future<? super V>> listener);

    @Override
    ProgressiveFuture<V> addListeners(GenericFutureListener... listeners);

    @Override
    ProgressiveFuture<V> removeListener(GenericFutureListener<? extends Future<? super V>> listener);

    @Override
    ProgressiveFuture<V> removeListeners(GenericFutureListener... listeners);

    @Override
    ProgressiveFuture<V> sync() throws InterruptedException;

    @Override
    ProgressiveFuture<V> syncUninterruptibly();

    @Override
    ProgressiveFuture<V> await() throws InterruptedException;

    @Override
    ProgressiveFuture<V> awaitUninterruptibly();
}

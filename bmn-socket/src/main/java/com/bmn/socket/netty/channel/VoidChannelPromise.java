package com.bmn.socket.netty.channel;

import com.bmn.socket.netty.util.concurrent.AbstractFuture;
import com.bmn.socket.netty.util.concurrent.Future;
import com.bmn.socket.netty.util.concurrent.GenericFutureListener;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/2/16.
 */
public class VoidChannelPromise extends AbstractFuture<Void> implements ChannelPromise {
    private final Channel channel;
    private final boolean fireException;


    VoidChannelPromise(Channel channel, boolean fireException) {
        if(channel == null) {
            throw new NullPointerException("channel");
        }
        this.channel = channel;
        this.fireException = fireException;
    }

    @Override
    public Channel channel() {
        return channel;
    }

    @Override
    public VoidChannelPromise setSuccess(Void var1) {
        return this;
    }

    @Override
    public boolean trySuccess(Void var1) {
        return false;
    }

    @Override
    public VoidChannelPromise setSuccess() {
        return this;
    }

    @Override
    public boolean trySuccess() {
        return false;
    }

    @Override
    public VoidChannelPromise setFailure(Throwable cause) {
        fireException(cause);
        return this;
    }

    @Override
    public boolean tryFailure(Throwable cause) {
        fireException(cause);
        return false;
    }

    @Override
    public boolean setUncancellable() {
        return true;
    }

    @Override
    public VoidChannelPromise sync() throws InterruptedException {
        fail();
        return this;
    }

    @Override
    public VoidChannelPromise syncUninterruptibly() {
        fail();
        return this;
    }

    @Override
    public VoidChannelPromise await() throws InterruptedException {
        if(Thread.interrupted()) {
            throw new InterruptedException();
        }
        return this;
    }

    @Override
    public VoidChannelPromise awaitUninterruptibly() {
        fail();
        return this;
    }

    @Override
    public boolean await(long var1, TimeUnit var3) throws InterruptedException {
        fail();
        return false;
    }

    @Override
    public boolean await(long var1) throws InterruptedException {
        fail();
        return false;
    }

    @Override
    public boolean awaitUninterruptibly(long var1, TimeUnit var3) {
        fail();
        return false;
    }

    @Override
    public boolean awaitUninterruptibly(long var1) {
        fail();
        return false;
    }

    @Override
    public Void getNow() {
        return null;
    }

    @Override
    public boolean cancel(boolean var1) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public boolean isCancellable() {
        return false;
    }

    @Override
    public Throwable cause() {
        return null;
    }

    @Override
    public VoidChannelPromise addListener(GenericFutureListener<? extends Future<? super Void>> var1) {
        fail();
        return this;
    }

    @Override
    public VoidChannelPromise addListeners(GenericFutureListener... var1) {
        fail();
        return this;
    }

    @Override
    public VoidChannelPromise removeListener(GenericFutureListener<? extends Future<? super Void>> var1) {
        return this;
    }

    @Override
    public VoidChannelPromise removeListeners(GenericFutureListener... var1) {
        return this;
    }

    @Override
    public ChannelPromise unvoid() {
        ChannelPromise promise = new DefaultChannelPromise(channel);
        if (fireException) {
            promise.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        fireException(future.cause());
                    }
                }
            });
        }
        return promise;
    }

    private static void fail() {
        throw new IllegalStateException("void future");
    }

    private void fireException(Throwable cause) {
        if (fireException && channel.isRegistered()) {
            channel.pipeline().fireExceptionCaught(cause);
        }
    }
}

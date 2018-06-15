package com.bmn.socket.netty.channel;

import com.bmn.socket.netty.channel.ChannelFlushPromiseNotifier.FlushCheckpoint;
import com.bmn.socket.netty.util.concurrent.DefaultPromise;
import com.bmn.socket.netty.util.concurrent.EventExecutor;
import com.bmn.socket.netty.util.concurrent.Future;
import com.bmn.socket.netty.util.concurrent.GenericFutureListener;

/**
 * Created by Administrator on 2017/1/13.
 */
public class DefaultChannelPromise extends DefaultPromise<Void> implements ChannelPromise, FlushCheckpoint {
    private final Channel channel;
    private long checkpoint;

    public DefaultChannelPromise(Channel channel) {
        this.channel = channel;
    }

    public DefaultChannelPromise(Channel channel, EventExecutor executor) {
        super(executor);
        this.channel = channel;
    }

    protected EventExecutor executor() {
        EventExecutor e = super.executor();
        if(e == null) {
            return channel().eventLoop();
        } else {
            return e;
        }
    }

    @Override
    public Channel channel() {
        return channel;
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public ChannelPromise setSuccess() {
        return setSuccess(null);
    }

    @Override
    public ChannelPromise setSuccess(Void result) {
        super.setSuccess(result);
        return this;
    }

    @Override
    public boolean trySuccess() {
        return trySuccess(null);
    }

    @Override
    public ChannelPromise setFailure(Throwable var1) {
        super.setFailure(var1);
        return this;
    }

    @Override
    public ChannelPromise addListener(GenericFutureListener<? extends Future<? super Void>> var1) {
        super.addListener(var1);
        return this;
    }

    @Override
    public ChannelPromise addListeners(GenericFutureListener... var1) {
        super.addListeners(var1);
        return this;
    }

    @Override
    public ChannelPromise removeListener(GenericFutureListener<? extends Future<? super Void>> var1) {
        super.removeListener(var1);
        return this;
    }

    @Override
    public ChannelPromise removeListeners(GenericFutureListener... var1) {
        super.removeListeners(var1);
        return this;
    }

    @Override
    public ChannelPromise sync() throws InterruptedException {
        super.sync();
        return this;
    }

    @Override
    public ChannelPromise syncUninterruptibly() {
        super.syncUninterruptibly();
        return this;
    }

    @Override
    public ChannelPromise awaitUninterruptibly() {
        super.awaitUninterruptibly();
        return this;
    }

    @Override
    public ChannelPromise await() throws InterruptedException {
        super.await();
        return this;
    }

    @Override
    public ChannelPromise unvoid() {
        return this;
    }

    @Override
    public long flushCheckpoint() {
        return checkpoint;
    }

    @Override
    public void flushCheckpoint(long checkpoint) {
        this.checkpoint = checkpoint;
    }

    @Override
    protected void checkDeadLock() {
        if (channel().isRegistered()) {
            super.checkDeadLock();
        }
    }

    @Override
    public ChannelPromise promise() {
        return this;
    }
}

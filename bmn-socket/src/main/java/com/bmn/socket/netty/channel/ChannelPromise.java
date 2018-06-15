package com.bmn.socket.netty.channel;

import com.bmn.socket.netty.util.concurrent.Future;
import com.bmn.socket.netty.util.concurrent.GenericFutureListener;
import com.bmn.socket.netty.util.concurrent.Promise;

/**
 * Created by Administrator on 2017/1/7.
 */
public interface ChannelPromise extends ChannelFuture, Promise<Void> {
    @Override
    Channel channel();

    ChannelPromise setSuccess(Void var1);
    ChannelPromise setSuccess();

    boolean trySuccess();

    ChannelPromise setFailure(Throwable var1);

    @Override
    ChannelPromise sync() throws InterruptedException;

    @Override
    ChannelPromise syncUninterruptibly();

    @Override
    ChannelPromise await() throws InterruptedException;

    @Override
    ChannelPromise awaitUninterruptibly();

    @Override
    ChannelPromise addListener(GenericFutureListener<? extends Future<? super Void>> var1);

    @Override
    ChannelPromise addListeners(GenericFutureListener... var1);

    @Override
    ChannelPromise removeListener(GenericFutureListener<? extends Future<? super Void>> var1);

    @Override
    ChannelPromise removeListeners(GenericFutureListener... var1);

    ChannelPromise unvoid();

}

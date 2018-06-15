package com.bmn.socket.netty.util.concurrent;

import com.bmn.socket.netty.channel.ChannelFuture;

/**
 * Created by Administrator on 2017/2/27.
 */
public interface ChannelProgressiveFuture extends ChannelFuture, ProgressiveFuture<Void>{

    @Override
    ChannelProgressiveFuture addListener(GenericFutureListener<? extends Future<? super Void>> var1);

    @Override
    ChannelProgressiveFuture addListeners(GenericFutureListener... var1);

    @Override
    ChannelProgressiveFuture removeListener(GenericFutureListener<? extends Future<? super Void>> var1);

    @Override
    ChannelProgressiveFuture removeListeners(GenericFutureListener... var1);

    @Override
    ChannelProgressiveFuture sync() throws InterruptedException;

    @Override
    ChannelProgressiveFuture syncUninterruptibly();

    @Override
    ChannelProgressiveFuture await() throws InterruptedException;

    @Override
    ChannelProgressiveFuture awaitUninterruptibly();
}

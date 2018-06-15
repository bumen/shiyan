package com.bmn.socket.netty.channel;

import com.bmn.socket.netty.util.concurrent.Future;
import com.bmn.socket.netty.util.concurrent.GenericFutureListener;

/**
 * Created by Administrator on 2017/1/5.
 */
public interface ChannelFuture extends Future<Void> {
    Channel channel();

    ChannelFuture addListener(GenericFutureListener<? extends Future<? super Void>> var1);

    ChannelFuture addListeners(GenericFutureListener... var1);

    ChannelFuture removeListener(GenericFutureListener<? extends Future<? super Void>> var1);

    ChannelFuture removeListeners(GenericFutureListener... var1);

    ChannelFuture sync() throws InterruptedException;

    ChannelFuture syncUninterruptibly();

    ChannelFuture await() throws InterruptedException;

    ChannelFuture awaitUninterruptibly();

    boolean isVoid();
}

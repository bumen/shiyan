package com.bmn.socket.netty.channel.group;

import com.bmn.socket.netty.channel.ChannelFuture;
import com.bmn.socket.netty.util.concurrent.Future;
import com.bmn.socket.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.group.ChannelGroupException;

import java.util.Iterator;
import java.util.logging.Filter;

/**
 * Created by Administrator on 2017/1/5.
 */
public interface ChannelGroupFuture extends Future<Void>, Iterable<ChannelFuture>{
   // ChannelGroup group();

   // ChannelFuture find(Channel var1);

    boolean isSuccess();

    ChannelGroupException cause();

    boolean isPartialSuccess();
    boolean isPartialFailure();

    ChannelGroupFuture addListener(GenericFutureListener<? extends Future<? super Void>> var1);

    ChannelGroupFuture addListeners(GenericFutureListener... var1);

    ChannelGroupFuture removeListener(GenericFutureListener<? extends Future<? super Void>> var1);

    ChannelGroupFuture removeListeners(GenericFutureListener... var1);

    ChannelGroupFuture sync() throws InterruptedException;

    @Override
    ChannelGroupFuture syncUninterruptibly();

    @Override
    ChannelGroupFuture await() throws InterruptedException;

    @Override
    ChannelGroupFuture awaitUninterruptibly();

    @Override
    Iterator<ChannelFuture> iterator();
}

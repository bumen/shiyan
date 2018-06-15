package com.bmn.socket.netty.channel;


import com.bmn.socket.netty.util.concurrent.ChannelProgressiveFuture;
import com.bmn.socket.netty.util.concurrent.Future;
import com.bmn.socket.netty.util.concurrent.GenericFutureListener;
import com.bmn.socket.netty.util.concurrent.ProgressivePromise;

/**
 * Created by Administrator on 2017/2/27.
 */
public interface ChannelProgressPromise extends ProgressivePromise<Void>, ChannelProgressiveFuture, ChannelPromise {
    @Override
    ChannelProgressPromise setSuccess();

    @Override
    ChannelProgressPromise setSuccess(Void result);

    @Override
    ChannelProgressPromise unvoid();

    @Override
    ChannelProgressPromise setFailure(Throwable cause);

    @Override
    ChannelProgressPromise addListener(GenericFutureListener<? extends Future<? super Void>> var1);

    @Override
    ChannelProgressPromise addListeners(GenericFutureListener... var1);

    @Override
    ChannelProgressPromise removeListener(GenericFutureListener<? extends Future<? super Void>> var1);

    @Override
    ChannelProgressPromise removeListeners(GenericFutureListener... var1);

    @Override
    ChannelProgressPromise await() throws InterruptedException;

    @Override
    ChannelProgressPromise awaitUninterruptibly();

    @Override
    ChannelProgressPromise sync() throws InterruptedException;

    @Override
    ChannelProgressPromise syncUninterruptibly();

    @Override
    ChannelProgressPromise setProgress(long progress, long total);

}

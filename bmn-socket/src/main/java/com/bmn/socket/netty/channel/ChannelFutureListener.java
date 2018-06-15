package com.bmn.socket.netty.channel;

import com.bmn.socket.netty.util.concurrent.GenericFutureListener;

/**
 * Created by Administrator on 2017/1/13.
 */
public interface ChannelFutureListener extends GenericFutureListener<ChannelFuture> {
    ChannelFutureListener CLOSE = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            future.channel().close();
        }
    };

    ChannelFutureListener CLOSE_ON_FAILURE = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if(!future.isSuccess()) {
                future.channel().close();
            }
        }
    };

    ChannelFutureListener FIRE_EXCEPTION_ON_FAILURE = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if(!future.isSuccess()) {
                future.channel();
            }
        }
    };
}

package com.bmn.socket.netty.channel;

import com.bmn.socket.netty.util.concurrent.EventExecutorGroup;

/**
 * Created by Administrator on 2017/1/6.
 */
public interface EventLoopGroup extends EventExecutorGroup {
    EventLoop next();

    ChannelFuture register(Channel channel);

    ChannelFuture register(ChannelPromise channelPromise);

}

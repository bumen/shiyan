package com.bmn.socket.netty.channel;

import com.bmn.socket.netty.util.concurrent.OrderedEventExecutor;

/**
 * Created by Administrator on 2017/1/7.
 */
public interface EventLoop extends OrderedEventExecutor, EventLoopGroup{
    EventLoopGroup parent();
}

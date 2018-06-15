package com.bmn.socket.netty.channel;

import com.bmn.socket.netty.util.concurrent.AbstractEventExecutorGroup;
import com.bmn.socket.netty.util.concurrent.EventExecutor;

/**
 * Created by Administrator on 2017/1/9.
 */
public abstract class AbstractEventLoopGroup extends AbstractEventExecutorGroup implements EventLoopGroup {
    @Override
    public abstract EventLoop next();
}

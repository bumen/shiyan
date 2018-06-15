package com.bmn.socket.netty.channel;


import com.bmn.socket.netty.util.concurrent.AbstractEventExecutor;

/**
 * Created by Administrator on 2017/1/6.
 */
public abstract class AbstractEventLoop extends AbstractEventExecutor implements EventLoop {

        protected AbstractEventLoop() {}

        protected AbstractEventLoop(EventLoopGroup parent) {
            super(parent);
        }

        public EventLoopGroup parent() {return (EventLoopGroup) super.parent();}

        public EventLoop next() {return (EventLoop) super.next();}




}

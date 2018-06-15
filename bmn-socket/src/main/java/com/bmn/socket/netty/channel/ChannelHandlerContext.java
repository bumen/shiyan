package com.bmn.socket.netty.channel;

import com.bmn.socket.netty.util.AttributeMap;
import com.bmn.socket.netty.util.concurrent.EventExecutor;
import io.netty.buffer.ByteBufAllocator;

/**
 * Created by Administrator on 2017/1/31.
 */
public interface ChannelHandlerContext extends AttributeMap, ChannelInboundInvoker, ChannelOutboundInvoker {
    Channel channel();

    EventExecutor executor();

    String name();

    ChannelHandler handler();

    boolean isRemoved();

    ChannelHandlerContext fireChannelRegistered();

    ChannelHandlerContext fireChannelUnregistered();

    ChannelHandlerContext fireChannelActive();

    ChannelHandlerContext fireChannelInactive();

    ChannelHandlerContext fireExceptionCaught(Throwable cause);

    ChannelHandlerContext fireUserEventTriggered(Object evt);

    ChannelHandlerContext fireChannelRead(Object msg);

    ChannelHandlerContext fireChannelReadComplete();

    ChannelHandlerContext fireChannelWritabilityChanged();

    ChannelHandlerContext read();

    ChannelHandlerContext flush();

    ChannelPipeline pipeline();

    ByteBufAllocator alloc();



}

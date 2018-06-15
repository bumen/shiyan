package com.bmn.socket.netty.channel;

import io.netty.buffer.ByteBufAllocator;

import java.util.Map;

/**
 * Created by Administrator on 2017/1/16.
 */
public interface ChannelConfig {
    Map<ChannelOption<?>, Object> getOptions();

    boolean setOptions(Map<ChannelOption<?>, ?> options);

    <T> T getOption(ChannelOption<T> option);

    <T> boolean setOption(ChannelOption<T> option, T value);

    int getConnectTimeoutMillis();

    ChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis);

    int getWriteSpinCount();

    ChannelConfig setWriteSpinCount(int writeSpinCount);

    ByteBufAllocator getAllocator();

    ChannelConfig setAllocator(ByteBufAllocator allocator);

    <T extends RecvByteBufAllocator> T getRecByteBufAllocator();

    ChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator);

    boolean isAutoRead();

    ChannelConfig setAutoRead(boolean autoRead);

    int getWriteBufferHighWaterMark();

    int getWriteBufferLowWaterMark();

    MessageSizeEstimator getMessageSizeEstimator();

    ChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator);

    WriteBufferWaterMark getWriteBufferWaterMark();

    ChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark);
}

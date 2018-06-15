package com.bmn.socket.netty.channel;

/**
 * Created by Administrator on 2017/1/13.
 */
public final class ChannelFlushPromiseNotifier {


    interface FlushCheckpoint {
        long flushCheckpoint();
        void flushCheckpoint(long checkpoint);
        ChannelPromise promise();
    }

}

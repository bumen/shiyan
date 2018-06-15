package com.bmn.socket.netty.channel;

import com.bmn.socket.netty.channel.Channel;

/**
 * Created by Administrator on 2017/1/13.
 */
public interface ChannelFactory<T extends Channel> {
    T newChannel();
}

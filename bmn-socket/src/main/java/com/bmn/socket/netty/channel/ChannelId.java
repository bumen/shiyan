package com.bmn.socket.netty.channel;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/16.
 */
public interface ChannelId extends Serializable, Comparable<ChannelId> {
    String asShortText();

    String asLongText();
}

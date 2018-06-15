package com.bmn.socket.netty.bootstrap;

import com.bmn.socket.netty.channel.ChannelHandler;
import com.bmn.socket.netty.channel.ChannelOption;
import com.bmn.socket.netty.channel.EventLoopGroup;
import com.bmn.socket.netty.channel.ServerChannel;
import com.bmn.socket.netty.util.AttributeKey;

import java.util.Map;

/**
 * Created by Administrator on 2017/1/16.
 */
public class ServerBootstrapConfig extends AbstractBootstrapConfig<ServerBootstrap, ServerChannel> {
    ServerBootstrapConfig(ServerBootstrap bootstrap) {
        super(bootstrap);
    }

    public ChannelHandler childHandler() {
        return null;
    }
    
    public Map<ChannelOption<?>, Object> childOptions() {
        return null;
    }

    public Map<AttributeKey<?>, Object> childAttrs() {
        return null;
    }

}

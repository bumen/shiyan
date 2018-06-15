package com.bmn.socket.netty.bootstrap;

import com.bmn.socket.netty.channel.Channel;
import com.bmn.socket.netty.channel.ChannelFactory;
import com.bmn.socket.netty.channel.ChannelHandler;
import com.bmn.socket.netty.channel.ChannelOption;
import com.bmn.socket.netty.util.AttributeKey;
import java.net.SocketAddress;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/13.
 */
public class AbstractBootstrapConfig<B extends AbstractBootstrap<B, C>, C extends Channel> {
    protected final B bootstrap;

    protected AbstractBootstrapConfig(B bootstrap) {
        this.bootstrap = bootstrap;
    }

    public final SocketAddress localAddress() {
        return bootstrap.localAddress();
    }

    public final ChannelFactory<? extends C> channelFactory(){return bootstrap.channelFactory();}

    public final ChannelHandler handler() {return bootstrap.handler();}

    public final Map<ChannelOption<?>, Object> options() {return bootstrap.options();}

    public final Map<AttributeKey<?>, Object> attrs(){return bootstrap.attrs();}


}

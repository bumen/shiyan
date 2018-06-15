package com.bmn.socket.netty.bootstrap;


import com.bmn.socket.netty.channel.Channel;
import java.net.SocketAddress;

/**
 * Created by Administrator on 2017/1/13.
 */
public class Bootstrap extends AbstractBootstrap<Bootstrap, Channel> {
    @Override
    void init(Channel channel) throws Exception {
        
    }

    @Override
    public AbstractBootstrapConfig<Bootstrap, Channel> config() {
        return null;
    }

    public SocketAddress remoteAddress() {
        return null;
    }
}

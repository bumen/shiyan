package com.bmn.socket.netty.bootstrap;

import com.bmn.socket.netty.channel.Channel;

import java.net.SocketAddress;

/**
 * Created by Administrator on 2017/1/13.
 */
public class BootstrapConfig extends AbstractBootstrapConfig<Bootstrap, Channel> {

    protected BootstrapConfig(Bootstrap bootstrap) {
        super(bootstrap);
    }

    public SocketAddress remoteAddress() {return bootstrap.remoteAddress();}



}

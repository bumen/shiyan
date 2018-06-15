package com.bmn.socket.netty.bootstrap;

import com.bmn.socket.netty.channel.*;
import com.bmn.socket.netty.channel.Channel;
import com.bmn.socket.netty.channel.ChannelConfig;
import com.bmn.socket.netty.channel.ChannelFuture;
import com.bmn.socket.netty.channel.ChannelFutureListener;
import com.bmn.socket.netty.channel.ChannelHandler;
import com.bmn.socket.netty.channel.ChannelHandlerAdapter;
import com.bmn.socket.netty.channel.ChannelHandlerContext;
import com.bmn.socket.netty.channel.ChannelInboundHandler;
import com.bmn.socket.netty.channel.ChannelInboundHandlerAdapter;
import com.bmn.socket.netty.channel.ChannelInitializer;
import com.bmn.socket.netty.channel.ChannelOption;
import com.bmn.socket.netty.channel.ChannelPipeline;
import com.bmn.socket.netty.channel.EventLoopGroup;
import com.bmn.socket.netty.channel.ServerChannel;
import com.bmn.socket.netty.util.AttributeKey;
import com.sun.corba.se.impl.ior.OldJIDLObjectKeyTemplate;
import io.netty.channel.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/1/16.
 */
public class ServerBootstrap extends AbstractBootstrap<ServerBootstrap, ServerChannel> {
    private final Map<ChannelOption<?>, Object> childOptions = new LinkedHashMap<>();
    private final Map<AttributeKey<?>, Object> childAttrs = new LinkedHashMap<>();
    private final ServerBootstrapConfig config = new ServerBootstrapConfig(this);
    private volatile EventLoopGroup childGroup;
    private volatile ChannelHandler childHandler;

    public ServerBootstrap(){}

    private ServerBootstrap(ServerBootstrap bootstrap) {
        super(bootstrap);

        childGroup = bootstrap.childGroup;
        childHandler = bootstrap.childHandler;
        synchronized (bootstrap.childOptions) {
            childOptions.putAll(bootstrap.childOptions);
        }

        synchronized (bootstrap.childAttrs) {
            childAttrs.putAll(bootstrap.childAttrs);
        }

    }

    public ServerBootstrap group(EventLoopGroup group) {
        return group(group, group);
    }

    public ServerBootstrap group(EventLoopGroup parentGroup, EventLoopGroup childGroup) {
        super.group(parentGroup);
        if(childGroup == null) {
            throw new NullPointerException();
        }
        if(this.childGroup != null) {
            throw new IllegalStateException();
        }
        this.childGroup = childGroup;
        return this;
    }

    public <T> ServerBootstrap childOption(ChannelOption<T> childOption, T value) {
        if(childOption == null) {
            throw new NullPointerException();
        }
        if(value == null) {
            synchronized (childOptions) {
                childOptions.remove(childOption);
            }
        } else {
            synchronized (childOptions) {
                childOptions.put(childOption, value);
            }
        }
        return this;
    }

    public <T> ServerBootstrap childAttr(AttributeKey<T> childKey, T value) {
        if(childAttrs == null) {
            throw new NullPointerException();
        }
        if(value == null) {
            synchronized (childAttrs) {
                childAttrs.remove(childAttrs);
            }
        } else {
            synchronized (childAttrs) {
                childAttrs.put(childKey, value);
            }
        }
        return this;
    }

    public ServerBootstrap childHandler(ChannelHandler handler) {
        if(childHandler == null) {
            throw new NullPointerException();
        }
        this.childHandler = childHandler;
        return this;
    }



    @Override
    void init(Channel channel) throws Exception {
        final Map<ChannelOption<?>, Object> options = options0();
        synchronized (options) {
            channel.config().setOptions(options);
        }

        final Map<AttributeKey<?>, Object> attrs = attrs0();
        synchronized (attrs) {
            for(Map.Entry<AttributeKey<?>, Object> e : attrs.entrySet()) {
                AttributeKey<Object> key = (AttributeKey<Object>) e.getKey();
                channel.attr(key).set(e.getValue());
            }
        }

        ChannelPipeline p = channel.pipeline();
        final EventLoopGroup currentChildGroup = childGroup;
        final ChannelHandler currentChildHandler = childHandler;
        final Map.Entry<ChannelOption<?>, Object>[] currentChildOptions;
        final Map.Entry<AttributeKey<?>, Object>[] currentChildAttrs;
        synchronized (childOptions) {
            currentChildOptions = childOptions.entrySet().toArray(newOptionArray(childOptions.size()));
        }

        synchronized (childAttrs) {
            currentChildAttrs = childAttrs.entrySet().toArray(newAttrArray(childAttrs.size()));
        }

        p.addLast(new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel ch) throws Exception {
                final ChannelPipeline pipeline = ch.pipeline();
                ChannelHandler handler = config.handler();
                if(handler != null) {
                    pipeline.addLast(handler);
                }

                ch.eventLoop().execute(new Runnable() {
                    @Override
                    public void run() {
                        pipeline.addLast(new ServerBootstrapAcceptor(currentChildGroup, currentChildHandler, currentChildOptions, currentChildAttrs));
                    }
                });
            }
        });
    }

    @Override
    public final ServerBootstrapConfig config() {
        return config;
    }

    private static class ServerBootstrapAcceptor extends ChannelInboundHandlerAdapter {
        private final EventLoopGroup childGroup;
        private final ChannelHandler childHandler;
        private final Map.Entry<ChannelOption<?>, Object>[] childOptions;
        private final Map.Entry<AttributeKey<?>, Object>[] childAttrs;

        ServerBootstrapAcceptor(EventLoopGroup childGroup, ChannelHandler childHandler,
                                Map.Entry<ChannelOption<?>, Object>[] childOptions, Map.Entry<AttributeKey<?>, Object>[] childAttrs) {
            this.childGroup = childGroup;
            this.childHandler = childHandler;
            this.childOptions = childOptions;
            this.childAttrs = childAttrs;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            final Channel child = (Channel) msg;
            child.pipeline().addLast(childHandler);

            for(Map.Entry<ChannelOption<?>, Object> e: childOptions) {
                try {
                    if(!child.config().setOption((ChannelOption<Object>) e.getKey(), e.getValue())) {

                    }
                } catch (Throwable t) {

                }
            }

            for(Map.Entry<AttributeKey<?>, Object> e : childAttrs) {
                child.attr((AttributeKey<Object> )e.getKey()).set(e.getKey());
            }

            try {
                childGroup.register(child).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if(!future.isSuccess()) {
                            forceClose(child, future.cause());
                        }
                    }
                });
            } catch (Throwable e) {
                forceClose(child, e);
            }

        }

        private static void forceClose(Channel child, Throwable t) {
            child.unsafe().closeForcibly();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            final ChannelConfig config = ctx.channel().config();
            if(config.isAutoRead()) {
                config.setAutoRead(false);
                ctx.channel().eventLoop().schedule(new Runnable() {
                    @Override
                    public void run() {
                        config.setAutoRead(true);
                    }
                }, 1, TimeUnit.SECONDS);
            }

            ctx.fireExceptionCaught(cause);
        }
    }



    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new ServerBootstrap(this);
    }

    private static Map.Entry<ChannelOption<?>, Object>[] newOptionArray(int size) {
        return new Map.Entry[size];
    }

    private static Map.Entry<AttributeKey<?>, Object>[] newAttrArray(int size) {
        return new Map.Entry[size];
    }


    public EventLoopGroup childGroup() {
        return childGroup;
    }

    public ChannelHandler childHandler() {
        return childHandler;
    }

    final Map<ChannelOption<?>, Object> childOptions() {
        return copiedMap(childOptions());
    }

    final Map<AttributeKey<?>, Object> childAttrs() {
        return copiedMap(childAttrs);
    }

}

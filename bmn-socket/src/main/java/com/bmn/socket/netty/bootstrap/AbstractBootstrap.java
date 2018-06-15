package com.bmn.socket.netty.bootstrap;


import com.bmn.socket.netty.channel.Channel;
import com.bmn.socket.netty.channel.ChannelFactory;
import com.bmn.socket.netty.channel.ChannelFuture;
import com.bmn.socket.netty.channel.ChannelFutureListener;
import com.bmn.socket.netty.channel.ChannelHandler;
import com.bmn.socket.netty.channel.ChannelOption;
import com.bmn.socket.netty.channel.ChannelPromise;
import com.bmn.socket.netty.channel.DefaultChannelPromise;
import com.bmn.socket.netty.channel.EventLoopGroup;
import com.bmn.socket.netty.channel.ReflectiveChannelFactory;
import com.bmn.socket.netty.util.AttributeKey;
import com.bmn.socket.netty.util.concurrent.EventExecutor;
import com.bmn.socket.netty.util.concurrent.GlobalEventExecutor;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/13.
 */
public abstract class AbstractBootstrap<B extends AbstractBootstrap<B, C>, C extends Channel> implements Cloneable {

     ThreadLocal<EventLoopGroup> group = new ThreadLocal<>();
    private volatile ChannelFactory< ? extends C> channelFactory;
    private volatile SocketAddress localAddress;
    private final Map<ChannelOption<?>, Object> options = new LinkedHashMap<>();
    private final Map<AttributeKey<?>, Object> attrs = new LinkedHashMap<>();
    private volatile ChannelHandler handler;

    AbstractBootstrap(){
        // Disallow extending from a different package.
    }

    AbstractBootstrap(AbstractBootstrap<B, C> bootstrap) {
        group = bootstrap.group;
        channelFactory = bootstrap.channelFactory;
        handler = bootstrap.handler;
        localAddress = bootstrap.localAddress;
        synchronized (bootstrap.options) {
            options.putAll(bootstrap.options);
        }

        synchronized (bootstrap.attrs) {
            attrs.putAll(bootstrap.attrs);
        }
    }

    public B group(EventLoopGroup group) {
        if(group == null) {
            throw new NullPointerException();
        }

        if(this.group.get() != null) {
            throw new IllegalStateException();
        }

        this.group.set(group);
        return (B)this;
    }

    public B channel(Class<? extends C> channelClass) {
        if(channelClass == null) {
            throw new NullPointerException();
        }

        return channelFactory(new ReflectiveChannelFactory<>(channelClass));
    }

    public B channelFactory(ChannelFactory<? extends  C> channelFactory) {
        if(channelFactory == null) {
            throw new NullPointerException();
        }
        if(this.channelFactory != null) {
            throw new IllegalStateException();
        }
        this.channelFactory = channelFactory;
        return (B)this;
    }

    public B localAddress(int inetPort) {
        return localAddress(new InetSocketAddress(inetPort));
    }

    public B localAddress(String inetHost, int inetPort) {
        return localAddress(new InetSocketAddress(inetHost, inetPort));
    }

    public B localAddress(InetAddress inetHost, int inetPort) {
        return localAddress(new InetSocketAddress(inetHost, inetPort));
    }

    public <T> B option(ChannelOption<T> option, T value) {
        if(option == null) {
            throw new NullPointerException();
        }

        if(value == null) {
            synchronized (options) {
                options.remove(option);
            }
        } else {
            synchronized (options) {
                options.put(option, value);
            }
        }
        return (B)this;
    }

    public <T> B attr(AttributeKey<T> key, T value) {
        if(key == null) {
            throw new NullPointerException();
        }

        if(value == null) {
            synchronized (attrs) {
                attrs.remove(key);
            }
        } else {
            synchronized (attrs) {
                attrs.put(key, value);
            }
        }
        return (B)this;
    }

    public B validate() {
        if(group.get() == null) {
            throw new IllegalStateException();
        }

        if(channelFactory == null) {
            throw new IllegalStateException();
        }

        return (B)this;
    }

    public ChannelFuture register() {
        validate();
        return initAndRegister();
    }

    public ChannelFuture bind() {
        validate();
        SocketAddress localAddress = this.localAddress;
        if(localAddress == null) {
            throw new IllegalStateException();
        }
        return doBind(localAddress);
    }

    public ChannelFuture bind(int inetPort) {
        return bind(new InetSocketAddress(inetPort));
    }

    public ChannelFuture bind(String inetHost, int inetPort) {
        return bind(new InetSocketAddress(inetHost, inetPort));
    }

    public ChannelFuture bind(InetAddress inetHost, int inetPort) {
        return bind(new InetSocketAddress(inetHost, inetPort));
    }

    public ChannelFuture bind(InetSocketAddress localAddress) {
        validate();
        if(localAddress == null) {
            throw new IllegalStateException();
        }
        return doBind(localAddress);
    }

    public ChannelFuture doBind(final SocketAddress localAddress) {
        final ChannelFuture regFuture = initAndRegister();
        final Channel channel = regFuture.channel();
        if(regFuture.cause() != null) {
            return regFuture;
        }

        if(regFuture.isDone()) {
            ChannelPromise promise = channel.newPromise();
            doBind0(regFuture, channel, localAddress, promise);
            return promise;
        } else {
            final PendingRegistrationPromise promise = new PendingRegistrationPromise(channel);
            regFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    Throwable cause = future.cause();
                    if(cause != null) {
                        promise.setFailure(cause);
                    } else {
                        promise.registered();
                        doBind0(regFuture, channel, localAddress, promise);
                    }
                }
            });
            return promise;
        }
    }

    final ChannelFuture initAndRegister() {
        Channel channel = null;
        try {
            channel = channelFactory.newChannel();
            init(channel);
        }catch (Throwable t) {
            if(channel != null) {
                channel.close();
            }
            return new DefaultChannelPromise(channel, GlobalEventExecutor.INSTNACE).setFailure(t);
        }

        ChannelFuture regFuture = null;

        return regFuture;
    }

    abstract void init(Channel channel) throws Exception;

    private static void doBind0(final ChannelFuture regFuture, final Channel channel, final SocketAddress localAddress, final ChannelPromise promise) {
        channel.eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                if(regFuture.isSuccess()) {
                    channel.bind(localAddress, promise);
                } else {
                    promise.setFailure(regFuture.cause());
                }
            }
        });
    }

    public B handler(ChannelHandler handler) {
        if(handler == null) {
            throw new NullPointerException();
        }
        this.handler = handler;

        return (B)this;
    }

    public abstract AbstractBootstrapConfig<B, C> config();

    final SocketAddress localAddress() {
        return this.localAddress;
    }

    final Map<AttributeKey<?>, Object> attrs0() {return this.attrs;}

    final Map<ChannelOption<?>, Object> options0() {return this.options;}

    final ChannelFactory<? extends C> channelFactory() {
        return this.channelFactory;
    }

    final ChannelHandler handler(){return this.handler;}

    final Map<AttributeKey<?>, Object> attrs() {
        return copiedMap(this.attrs);
    }

    final Map<ChannelOption<?>, Object> options() {
        return copiedMap(this.options);
    }

    static <K, V> Map<K, V> copiedMap(Map<K,V> map) {
        final Map<K, V> copied;
        synchronized (map) {
            if(map.isEmpty()) {
                return Collections.emptyMap();
            }
            copied = new LinkedHashMap<>(map);
        }
        return Collections.unmodifiableMap(copied);
    }

    public B localAddress(SocketAddress localAddress) {
        this.localAddress = localAddress;
        return (B)this;
    }




    static final class PendingRegistrationPromise extends DefaultChannelPromise {
        private volatile boolean registered;

        PendingRegistrationPromise(Channel channel) {
            super(channel);
        }

        void registered() {registered = true;}

        protected EventExecutor executor() {
            if(registered) {
                return super.executor();
            }

            return GlobalEventExecutor.INSTNACE;
        }

    }

}

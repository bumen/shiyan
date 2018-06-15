package com.bmn.socket.zmq.src;

import zmq.Msg;

import java.nio.charset.Charset;

/**
 * Created by Administrator on 2017/6/19.
 */
public class QvpZmq {
    /*  Context options  */
    public static final int ZMQ_IO_THREADS = 1;
    public static final int ZMQ_MAX_SOCKETS = 2;

    /*  Default for new contexts                                                  */
    public static final int ZMQ_IO_THREADS_DFLT = 1;
    public static final int ZMQ_MAX_SOCKETS_DFLT = 1024;

    /******************************************************************************/
    /*  0MQ socket definition.                                                    */
    /******************************************************************************/

    /*  Socket types.                                                             */
    public static final int ZMQ_PAIR = 0;
    public static final int ZMQ_PUB = 1;
    public static final int ZMQ_SUB = 2;
    public static final int ZMQ_REQ = 3;
    public static final int ZMQ_REP = 4;
    public static final int ZMQ_DEALER = 5;
    public static final int ZMQ_ROUTER = 6;
    public static final int ZMQ_PULL = 7;
    public static final int ZMQ_PUSH = 8;
    public static final int ZMQ_XPUB = 9;
    public static final int ZMQ_XSUB = 10;

    /*  Socket options.                                                           */
    public static final int ZMQ_AFFINITY = 4;
    public static final int ZMQ_IDENTITY = 5;
    public static final int ZMQ_SUBSCRIBE = 6;
    public static final int ZMQ_UNSUBSCRIBE = 7;
    public static final int ZMQ_RATE = 8;
    public static final int ZMQ_RECOVERY_IVL = 9;
    public static final int ZMQ_SNDBUF = 11;
    public static final int ZMQ_RCVBUF = 12;
    public static final int ZMQ_RCVMORE = 13;
    public static final int ZMQ_FD = 14;
    public static final int ZMQ_EVENTS = 15;
    public static final int ZMQ_TYPE = 16;
    public static final int ZMQ_LINGER = 17;
    public static final int ZMQ_RECONNECT_IVL = 18;
    public static final int ZMQ_BACKLOG = 19;
    public static final int ZMQ_RECONNECT_IVL_MAX = 21;
    public static final int ZMQ_MAXMSGSIZE = 22;
    public static final int ZMQ_SNDHWM = 23;
    public static final int ZMQ_RCVHWM = 24;
    public static final int ZMQ_MULTICAST_HOPS = 25;
    public static final int ZMQ_RCVTIMEO = 27;
    public static final int ZMQ_SNDTIMEO = 28;
    public static final int ZMQ_IPV4ONLY = 31;
    public static final int ZMQ_LAST_ENDPOINT = 32;
    public static final int ZMQ_ROUTER_MANDATORY = 33;
    public static final int ZMQ_TCP_KEEPALIVE = 34;
    public static final int ZMQ_TCP_KEEPALIVE_CNT = 35;
    public static final int ZMQ_TCP_KEEPALIVE_IDLE = 36;
    public static final int ZMQ_TCP_KEEPALIVE_INTVL = 37;
    public static final int ZMQ_TCP_ACCEPT_FILTER = 38;
    public static final int ZMQ_DELAY_ATTACH_ON_CONNECT = 39;
    public static final int ZMQ_XPUB_VERBOSE = 40;
    public static final int ZMQ_REQ_CORRELATE = 52;
    public static final int ZMQ_REQ_RELAXED = 53;
    // TODO: more constants
    public static final int ZMQ_ROUTER_HANDOVER = 56;
    public static final int ZMQ_XPUB_NODROP = 69;
    public static final int ZMQ_BLOCKY = 70;
    public static final int ZMQ_XPUB_VERBOSE_UNSUBSCRIBE = 78;


    public static final int ZMQ_POLLIN = 1;
    public static final int ZMQ_POLLOUT = 2;
    public static final int ZMQ_POLLERR = 4;


    /*  Send/recv options.                                                        */
    public static final int ZMQ_DONTWAIT = 1;
    public static final int ZMQ_SNDMORE = 2;


    public static final Charset CHARSET = Charset.forName("UTF-8");

    public static class Event{}

    public static QvpCtx createContext() {
        QvpCtx ctx = new QvpCtx();
        return ctx;
    }

    public static void destroyContext(QvpCtx ctx) {
        ctx.terminate();
    }


    public static void setContextOption(QvpCtx ctx, int option, int optval)
    {
        if (ctx == null || !ctx.checkTag()) {
            throw new IllegalStateException();
        }
        ctx.set(option, optval);
    }

    public static int getContextOption(QvpCtx ctx, int option)
    {
        if (ctx == null || !ctx.checkTag()) {
            throw new IllegalStateException();
        }
        return ctx.get(option);
    }

    public static QvpCtx init(int ioThreads)
    {
        if (ioThreads >= 0) {
            QvpCtx ctx = createContext();
            setContextOption(ctx, ZMQ_IO_THREADS, ioThreads);
            return ctx;
        }
        throw new IllegalArgumentException("io_threds must not be negative");
    }

    public static void term(QvpCtx ctx)
    {
        destroyContext(ctx);
    }

    public static QvpSocketBase socket(QvpCtx ctx, int type)
    {
        if (ctx == null || !ctx.checkTag()) {
            throw new IllegalStateException();
        }
        QvpSocketBase s = ctx.createSocket(type);
        return s;
    }

    public static void close(QvpSocketBase s)
    {
        if (s == null || !s.checkTag()) {
            throw new IllegalStateException();
        }
        s.close();
    }

    public static void setSocketOption(QvpSocketBase s, int option, Object optval)
    {
        if (s == null || !s.checkTag()) {
            throw new IllegalStateException();
        }

        s.setSocketOpt(option, optval);

    }

    public static Object getSocketOptionExt(QvpSocketBase s, int option)
    {
        if (s == null || !s.checkTag()) {
            throw new IllegalStateException();
        }

        return s.getsockoptx(option);
    }

    public static int getSocketOption(QvpSocketBase s, int opt)
    {
        return s.getSocketOpt(opt);
    }

    public static boolean bind(QvpSocketBase s, final String addr)
    {
        if (s == null || !s.checkTag()) {
            throw new IllegalStateException();
        }

        return s.bind(addr);
    }

    public static boolean connect(QvpSocketBase s, String addr)
    {
        if (s == null || !s.checkTag()) {
            throw new IllegalStateException();
        }
        return s.connect(addr);
    }

    public static boolean unbind(QvpSocketBase s, String addr)
    {
        if (s == null || !s.checkTag()) {
            throw new IllegalStateException();
        }
        return s.termEndpoint(addr);
    }

    public static boolean disconnect(QvpSocketBase s, String addr)
    {
        if (s == null || !s.checkTag()) {
            throw new IllegalStateException();
        }
        return s.termEndpoint(addr);
    }

    public static int send(QvpSocketBase s, String str, int flags)
    {
        byte [] data = str.getBytes(CHARSET);
        return send(s, data, data.length, flags);
    }

    public static int send(QvpSocketBase s, QvpMsg msg, int flags)
    {
        int rc = sendMsg(s, msg, flags);
        if (rc < 0) {
            return -1;
        }

        return rc;
    }

    public static int send(QvpSocketBase s, byte[] buf, int len, int flags)
    {
        if (s == null || !s.checkTag()) {
            throw new IllegalStateException();
        }

        QvpMsg msg = new QvpMsg(len);
        msg.put(buf, 0, len);

        int rc = sendMsg(s, msg, flags);
        if (rc < 0) {
            return -1;
        }

        return rc;
    }

    public int sendiov(QvpSocketBase s, byte[][] a, int count, int flags)
    {
        if (s == null || !s.checkTag()) {
            throw new IllegalStateException();
        }
        int rc = 0;
        QvpMsg msg;

        for (int i = 0; i < count; ++i) {
            msg = new QvpMsg(a[i]);
            if (i == count - 1) {
                flags = flags & ~ZMQ_SNDMORE;
            }
            rc = sendMsg(s, msg, flags);
            if (rc < 0) {
                rc = -1;
                break;
            }
        }
        return rc;

    }

    public static int sendMsg(QvpSocketBase s, QvpMsg msg, int flags)
    {
        int sz = msgSize(msg);
        boolean rc = s.send(msg, flags);
        if (!rc) {
            return -1;
        }
        return sz;
    }

    public static Msg recv(QvpSocketBase s, int flags)
    {
        if (s == null || !s.checkTag()) {
            throw new IllegalStateException();
        }
        Msg msg = recvMsg(s, flags);
        if (msg == null) {
            return null;
        }

        //  At the moment an oversized message is silently truncated.
        //  TODO: Build in a notification mechanism to report the overflows.
        //int to_copy = nbytes < len_ ? nbytes : len_;

        return msg;
    }

    public int recviov(QvpSocketBase s, byte[][] a, int count, int flags)
    {
        if (s == null || !s.checkTag()) {
            throw new IllegalStateException();
        }

        int nread = 0;
        boolean recvmore = true;

        for (int i = 0; recvmore && i < count; ++i) {
            // Cheat! We never close any msg
            // because we want to steal the buffer.
            Msg msg = recvMsg(s, flags);
            if (msg == null) {
                nread = -1;
                break;
            }

            // Cheat: acquire zmq_msg buffer.
            a[i] = msg.data();

            // Assume zmq_socket ZMQ_RVCMORE is properly set.
            recvmore = msg.hasMore();
        }
        return nread;
    }

    public static Msg recvMsg(QvpSocketBase s, int flags)
    {
        return s.recv(flags);
    }

    public static int msgSize(QvpMsg msg)
    {
        return msg.size();
    }
}

package com.bmn.socket.netty.util;

import com.bmn.socket.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2017/2/21.
 */
public final class ReferenceCountUtil {

    private static final Logger logger = LoggerFactory.getLogger(ReferenceCountUtil.class);

    public static <T> T retain(T msg) {
        if (msg instanceof ReferenceCounted) {
            return (T) ((ReferenceCounted)msg).retain();
        }
        return msg;
    }

    public static <T> T retain(T msg, int increment) {
        if (msg instanceof ReferenceCounted) {
            return (T) ((ReferenceCounted)msg).retain(increment);
        }
        return msg;
    }

    public static <T> T touch(T msg) {
        if (msg instanceof ReferenceCounted) {
            return (T) ((ReferenceCounted)msg).touch();
        }
        return msg;
    }

    public static <T> T touch(T msg, Object hint) {
        if (msg instanceof ReferenceCounted) {
            return (T) ((ReferenceCounted) msg).touch(hint);
        }
        return msg;
    }

    public static boolean release(Object msg) {
        if (msg instanceof ReferenceCounted) {
            return ((ReferenceCounted) msg).release();
        }
        return false;
    }

    public static boolean release(Object msg, int decrement) {
        if (msg instanceof ReferenceCounted) {
            return ((ReferenceCounted) msg).release(decrement);
        }
        return false;
    }

    public static void safeRelease(Object msg) {
        try {
            release(msg);
        } catch (Throwable throwable) {
            logger.warn("Failed to release a message: {}", msg, throwable);
        }
    }

    public static void safeRelease(Object msg, int decrement) {
        try {
            release(msg, decrement);
        } catch (Throwable t) {
            if (logger.isWarnEnabled()) {
                logger.warn("Failed to release a message: {} (decrement: {})", msg, decrement, t);
            }
        }
    }

    public static <T> T releaseLater(T msg) {
        return releaseLater(msg, 1);
    }

    public static <T> T releaseLater(T msg, int decrement) {
        if (msg instanceof ReferenceCounted) {
            ThreadDeadWatcher.watch(Thread.currentThread(), new ReleasingTask((ReferenceCounted)msg, decrement));
        }
        return msg;
    }

    private static final class ReleasingTask implements Runnable {
        private final ReferenceCounted obj;
        private final int decrement;

        ReleasingTask(ReferenceCounted obj, int decrement) {
            this.obj = obj;
            this.decrement = decrement;
        }

        @Override
        public void run() {
            try {
                if (!obj.release(decrement)) {
                    logger.warn("Non-zero refCnt: {}", this);
                } else {
                    logger.warn("Released: {}", this);
                }
            } catch (Exception ex) {
                logger.warn("Failed to release an object: {}", obj, ex);
            }
        }

        @Override
        public String toString() {
            return StringUtil.simpleClassName(obj) + ".release(" + decrement +") refCnt: " + obj.refCnt();
        }
    }

}

package com.bmn.socket.netty.util.internal;

/**
 * Created by Administrator on 2017/1/5.
 */
public final class PlatformDependent {

    public static void throwException(Throwable t) {
        PlatformDependent.<RuntimeException>throwException0(t);
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void throwException0(Throwable t) throws E {
        throw (E)t;
    }

}

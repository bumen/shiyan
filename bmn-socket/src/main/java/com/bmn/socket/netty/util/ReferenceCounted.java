package com.bmn.socket.netty.util;

/**
 * Created by Administrator on 2017/2/21.
 */
public interface ReferenceCounted {

    int refCnt();

    ReferenceCounted retain();

    ReferenceCounted retain(int increment);

    ReferenceCounted touch();

    ReferenceCounted touch(Object hint);

    boolean release();

    boolean release(int decrement);
}

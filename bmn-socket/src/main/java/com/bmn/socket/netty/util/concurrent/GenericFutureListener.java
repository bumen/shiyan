package com.bmn.socket.netty.util.concurrent;

import java.util.EventListener;

/**
 * Created by Administrator on 2017/1/5.
 */
public interface GenericFutureListener<F extends Future<?>> extends EventListener{
    void operationComplete(F var1) throws Exception;
}

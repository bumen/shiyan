package com.bmn.socket.netty.util;

/**
 * Created by Administrator on 2017/2/21.
 */
public interface ResourceLeak {
    void record();

    void record(Object hint);

    boolean close();
}

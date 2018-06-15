package com.bmn.socket.zmq.src;

/**
 * Created by Administrator on 2017/6/16.
 */
public class QvpOptions {
    public int linger = 0;
    public int socketId = 0;
    public long affinity = 0;

    public void setSocketOpt(int option, Object optval) {

    }

    public Object getsockopt(int option) {
        return null;
    }
}

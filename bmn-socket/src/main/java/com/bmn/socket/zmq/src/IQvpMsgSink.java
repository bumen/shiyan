package com.bmn.socket.zmq.src;

/**
 * Created by Administrator on 2017/6/16.
 */
public interface IQvpMsgSink {
    public int pushMsg(QvpMsg msg);
}

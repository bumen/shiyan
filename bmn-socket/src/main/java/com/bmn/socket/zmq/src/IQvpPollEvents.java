package com.bmn.socket.zmq.src;

/**
 * Created by Administrator on 2017/6/14.
 */
public interface IQvpPollEvents {
    // Called by I/O thread when file descriptor is ready for reading.
    void inEvent();

    // Called by I/O thread when file descriptor is ready for writing.
    void outEvent();

    // Called by I/O thread when file descriptor might be ready for connecting.
    void connectEvent();

    // Called by I/O thread when file descriptor is ready for accept.
    void acceptEvent();

    // Called when timer expires.
    void timerEvent(int id);
}

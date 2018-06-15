package com.bmn.socket.zmq;

import zmq.YPipe;

/**
 * Created by Administrator on 2017/6/12.
 */
public class YPipeTest {
    public static void main(String[] args) {
        YPipe<String> pipe = new YPipe<>(10);
        pipe.write("a", false);
        pipe.write("b", false);
        pipe.write("c", false);

       // pipe.unwrite();
        //pipe.flush();

        pipe.read();
        pipe.read();
    }
}

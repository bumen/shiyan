package com.bmn.socket.zmq;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import zmq.Ctx;
import zmq.SocketBase;

import java.util.Collections;

/**
 * Created by Administrator on 2017/6/13.
 */
public class Node {
    public static void main(String[] args) {
        ZContext zmqContext = new ZContext();

        ZMQ.Socket zmqPull = zmqContext.createSocket(ZMQ.PULL);


        Ctx ctx = new Ctx();
        ctx.set(zmq.ZMQ.ZMQ_IO_THREADS, 1);

        SocketBase socket =  ctx.createSocket(ZMQ.PULL);

        socket.bind("tcp://localhost:8088");
    }
}

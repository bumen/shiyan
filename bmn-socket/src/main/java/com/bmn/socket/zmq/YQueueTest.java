package com.bmn.socket.zmq;

import zmq.YQueue;

/**
 * Created by Administrator on 2017/6/9.
 */
public class YQueueTest {

    public static void main(String[] args) {
        YQueue<String> queue = new YQueue<>(10);

        queue.push("a");

        String s = queue.back();
        if(s == null) {
            System.out.println("null");
        }
        System.out.println(s);
    }
}

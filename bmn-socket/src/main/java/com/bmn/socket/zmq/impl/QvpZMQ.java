package com.bmn.socket.zmq.impl;

import com.bmn.socket.zmq.src.QvpCtx;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Administrator on 2017/6/19.
 */
public class QvpZMQ {

    public static class Context implements Closeable {

        private final AtomicBoolean closed = new AtomicBoolean(false);
        //private final QvpCtx ctx;

        protected Context(int ioThreads) {
         //   ctx =
        }


        @Override
        public void close() throws IOException {

        }
    }
}

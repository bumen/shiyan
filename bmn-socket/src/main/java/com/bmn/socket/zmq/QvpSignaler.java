package com.bmn.socket.zmq;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017/6/13.
 */
public class QvpSignaler implements Closeable {
    private final Pipe.SinkChannel w;
    private final Pipe.SourceChannel r;
    private final Selector selector;
    private final ByteBuffer rdumpy = ByteBuffer.allocate(1);

    private final AtomicInteger wcursor = new AtomicInteger(0);
    private int rcursor = 0;

    public QvpSignaler() {
        Pipe pipe = null;
        try {
            pipe = Pipe.open();
        } catch (IOException e) {
            e.printStackTrace();
        }

        r = pipe.source();
        w = pipe.sink();

        try {
            w.configureBlocking(false);
            r.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Selector selectorCopy = null;
        try {
            selectorCopy = Selector.open();
            r.register(selectorCopy, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.selector = selectorCopy;

    }

    public SelectableChannel getFd() {
        return r;
    }

    public void send() {
        int nbytes = 0;
        ByteBuffer dummy = ByteBuffer.allocate(1);
        while (true) {
            try {
                Thread.interrupted();
                nbytes = w.write(dummy);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nbytes == 0) {
                continue;
            }

            wcursor.incrementAndGet();
            break;
        }
    }

    public boolean waitEvent(long timeout) {
        int rc = 0;
        try {
            if (timeout == 0) {
                return rcursor < wcursor.get();
            } else if (timeout < 0) {
                rc = selector.select();
            } else {
                rc = selector.select(timeout);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(rc == 0) {
            return false;
        }

        selector.selectedKeys().clear();
        return true;
    }

    public void recv() {
        int nbytes = 0;
        while (nbytes == 0) {
            try {
                nbytes = r.read(rdumpy);
                rdumpy.rewind();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        rcursor++;
    }

    @Override
    public void close() throws IOException {

    }
}

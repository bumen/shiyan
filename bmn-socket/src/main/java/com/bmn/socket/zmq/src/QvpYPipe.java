package com.bmn.socket.zmq.src;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017/6/16.
 */
public class QvpYPipe<T> {

    private final QvpYQueue<T> queue;

    private int w;
    private int r;
    private int f;

    private final AtomicInteger c;

    public QvpYPipe(int qsize) {
        queue = new QvpYQueue<>(qsize);
        int pos = queue.backPos();
        f = pos;
        r = pos;
        w = pos;
        c = new AtomicInteger(pos);
    }

    public void write(final T value, boolean incomplete) {
        queue.push(value);
        if (!incomplete) {
            f = queue.backPos();
        }
    }

    public T unwrite() {
        if (f == queue.backPos()) {
            return null;
        }
        queue.unpush();
        return queue.back();
    }

    public boolean flush() {
        if(w == f) {
            return true;
        }

        if(!c.compareAndSet(w, f)) {
            c.set(f);
            w = f;
            return false;
        }

        w = f;
        return true;
    }

    public boolean checkRead() {
        int h = queue.backPos();
        if(h != r) {
            return true;
        }

        if (c.compareAndSet(h, -1)) {

        } else {
            r = c.get();
        }

        if(h == r || r == -1) {
            return false;
        }
        return true;
    }

    public T read() {
        if (!checkRead()) {
            return null;
        }

        return queue.pop();
    }

    public T probe() {
        boolean rc = checkRead();
        return queue.front();
    }
}

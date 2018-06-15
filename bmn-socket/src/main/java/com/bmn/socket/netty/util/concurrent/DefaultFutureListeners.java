package com.bmn.socket.netty.util.concurrent;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/1/5.
 */
public final class DefaultFutureListeners {
    private GenericFutureListener<? extends Future<?>> [] listeners = new GenericFutureListener[2];
    private int size;

    public DefaultFutureListeners(GenericFutureListener<? extends Future<?>> first, GenericFutureListener<? extends Future<?>> second) {
        this.listeners[0] = first;
        this.listeners[1] = second;
        this.size = 2;
    }

    public void add(GenericFutureListener<? extends Future<?>> l) {
        GenericFutureListener[] listeners = this.listeners;
        int size = this.size;

        if(size == listeners.length) {
            this.listeners = listeners = Arrays.copyOf(listeners, size << 1);
        }

        listeners[size]  = l;
        this.size = size + 1;
    }

    public void remove(GenericFutureListener<? extends Future<?>> l) {
        GenericFutureListener[] listeners = this.listeners;
        int size = this.size;
        for(int i = 0; i < size; ++i) {
            if(listeners[i] == l) {
                int toMove = size - i - 1;
                if(toMove > 0) {
                    System.arraycopy(listeners, i + 1, listeners, i, toMove);
                }
                --size;
                listeners[size] = null;
                this.size = size;
                return;
            }
        }
    }

    public GenericFutureListener<? extends Future<?>>[] listeners() {return this.listeners;}

    public int size(){return this.size;}

}

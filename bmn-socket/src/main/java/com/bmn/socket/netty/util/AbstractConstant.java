package com.bmn.socket.netty.util;

import java.nio.ByteBuffer;

/**
 * Created by Administrator on 2017/1/13.
 */
public class AbstractConstant<T extends AbstractConstant<T>> implements Constant<T> {
    private final int id;
    private final String name;
    private volatile long uniquifier;
    private ByteBuffer directBuffer;
    protected AbstractConstant(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public final int id() {
        return id;
    }

    @Override
    public final String name() {
        return name;
    }
    @Override
    public final String toString() {
        return name();
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int compareTo(T o) {
        return 0;
    }
}

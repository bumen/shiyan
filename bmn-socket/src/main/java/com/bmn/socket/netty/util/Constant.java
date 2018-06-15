package com.bmn.socket.netty.util;

/**
 * Created by Administrator on 2017/1/13.
 */
public interface Constant< T extends Constant<T>> extends Comparable<T> {
    int id();
    String name();
}

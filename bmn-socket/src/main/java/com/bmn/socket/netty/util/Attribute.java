package com.bmn.socket.netty.util;

/**
 * Created by Administrator on 2017/1/13.
 */
public interface Attribute<T> {
    AttributeKey<T> key();


    T get();
    void set(T value);
    T getAndSet(T value);
    T setIfAbsent(T value);
    T getAndRemove();
    boolean compareAndSet(T oldValue, T newValue);
    void remove();

}

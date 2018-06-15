package com.bmn.socket.netty.util;

/**
 * Created by Administrator on 2017/1/13.
 */
public interface AttributeMap {
    <T> Attribute<T> attr(AttributeKey<T> key);

    <T> boolean hasAttr(AttributeKey<T> key);
}

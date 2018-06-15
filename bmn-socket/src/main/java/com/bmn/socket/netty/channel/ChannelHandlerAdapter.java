package com.bmn.socket.netty.channel;

import java.util.Map;

/**
 * Created by Administrator on 2017/1/16.
 */
public class ChannelHandlerAdapter implements ChannelHandler{
    boolean added;

    public boolean isSharable() {
        Class<?> clazz = getClass();
        Map<Class<?>, Boolean> cache = null;
        Boolean sharable = cache.get(clazz);
        if(sharable == null) {
            sharable = clazz.isAnnotationPresent(Sharable.class);
            cache.put(clazz, sharable);
        }
        return sharable;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

    }
}

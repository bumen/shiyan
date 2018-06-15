package com.bmn.socket.netty.util.concurrent;

/**
 * Created by Administrator on 2017/1/9.
 */
public class SucceededFuture<V> extends CompleteFuture<V>{
    private final V result;

    public SucceededFuture(EventExecutor executor, V result) {
        super(executor);

        this.result = result;
    }

    public Throwable cause() {return null;}

    public boolean isSuccess(){return true;}

    public V getNow(){return result;}
}

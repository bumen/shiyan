package com.bmn.socket.netty.util.concurrent;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/**
 * Created by Administrator on 2017/1/10.
 */
public class ThreadPerTaskExecutor implements Executor {
    private final ThreadFactory threadFactory;


    public ThreadPerTaskExecutor(ThreadFactory threadFactory) {
        if(threadFactory == null) {
            throw new NullPointerException("thread factory");
        }
        this.threadFactory = threadFactory;
    }
    @Override
    public void execute(Runnable command) {
        threadFactory.newThread(command).start();
    }
}

package com.bmn.http.httpclient.impl.server;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Administrator on 2017/5/24.
 */
public class ThreadFactoryImpl implements ThreadFactory {
    private final String namePrefix;
    private final ThreadGroup group;
    private final AtomicLong count;

    public ThreadFactoryImpl(String namePrefix, ThreadGroup group) {
        this.namePrefix = namePrefix;
        this.group = group;
        this.count = new AtomicLong();
    }
    public ThreadFactoryImpl(String namePrefix) {
        this(namePrefix, null);
    }


    @Override
    public Thread newThread(Runnable r) {
        return new Thread(this.group, r, this.namePrefix + "-" + this.count.incrementAndGet());
    }
}

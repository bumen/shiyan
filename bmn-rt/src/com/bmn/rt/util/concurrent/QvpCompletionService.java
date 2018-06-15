package com.bmn.rt.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/10/13.
 */
public class QvpCompletionService<V> implements CompletionService<V> {
    @Override
    public Future<V> submit(Callable<V> task) {
        return null;
    }

    @Override
    public Future<V> submit(Runnable task, V result) {
        return null;
    }

    @Override
    public Future<V> take() throws InterruptedException {
        return null;
    }

    @Override
    public Future<V> poll() {
        return null;
    }

    @Override
    public Future<V> poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }
}

package com.bmn.socket.netty.util.concurrent;

/**
 * Created by Administrator on 2017/1/10.
 */
public interface EventExecutorChooserFactory {

    EventExecutorChooser newChooser(EventExecutor[] executors);

    interface EventExecutorChooser {
        EventExecutor next();
    }
}

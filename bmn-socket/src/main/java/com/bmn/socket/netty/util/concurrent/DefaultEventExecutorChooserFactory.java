package com.bmn.socket.netty.util.concurrent;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017/1/10.
 */
public class DefaultEventExecutorChooserFactory implements EventExecutorChooserFactory {
    public static final DefaultEventExecutorChooserFactory INSTNACE = new DefaultEventExecutorChooserFactory();

    private DefaultEventExecutorChooserFactory(){}
    @Override
    public EventExecutorChooser newChooser(EventExecutor[] executors) {
        if(isPowerOfTwo(executors.length)) {
            return new PowerOfTowEventExecutorChooser(executors);
        }
        return new GenericEventExecutorChooser(executors);
    }

    private static boolean isPowerOfTwo(int val) {
        return (val & -val) == val;
    }

    private static final class PowerOfTowEventExecutorChooser implements EventExecutorChooser {
        private final AtomicInteger idx = new AtomicInteger();
        private final EventExecutor[] executors;

        PowerOfTowEventExecutorChooser(EventExecutor[] executors) {
            this.executors = executors;
        }

        @Override
        public EventExecutor next() {
            return executors[idx.getAndIncrement() & executors.length - 1];
        }
    }

    private static final class GenericEventExecutorChooser implements EventExecutorChooser {
        private final AtomicInteger idx = new AtomicInteger();
        private final EventExecutor[] executors;

        GenericEventExecutorChooser(EventExecutor[] executors) {
            this.executors = executors;
        }

        @Override
        public EventExecutor next() {
            return executors[Math.abs(idx.getAndIncrement() % executors.length)];
        }

    }
}

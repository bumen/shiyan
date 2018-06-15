package com.bmn.socket.netty.util;

import com.bmn.socket.netty.util.concurrent.DefaultThreadFactory;
import com.bmn.socket.netty.util.internal.PlatformDependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Administrator on 2017/2/21.
 */
public final class ThreadDeadWatcher {
    private static final Logger logger = LoggerFactory.getLogger(ThreadDeadWatcher.class);

    static final ThreadFactory threadFactory;

    private static final Queue<Entry> pendingEntries = io.netty.util.internal.PlatformDependent.newMpscQueue();
    private static final AtomicBoolean started = new AtomicBoolean();
    private static final Watcher watcher = new Watcher();
    private static volatile Thread watcherThread;

    static {
        String poolName = "threadDeadWatcher";

        threadFactory = new DefaultThreadFactory(poolName, true, Thread.MIN_PRIORITY, null);
    }

    public static void watch(Thread thread, Runnable task) {
        if (thread == null) {
            throw new NullPointerException("thread");
        }
        if (task == null) {
            throw new NullPointerException("task");
        }
        if (!thread.isAlive()) {
            throw new IllegalArgumentException("thread must be alive.");
        }

        schedule(thread, task, true);
    }

    public static void unwatch(Thread thread, Runnable task) {
        if (thread == null) {
            throw new NullPointerException("thread");
        }
        if (task == null) {
            throw new NullPointerException("task");
        }
        schedule(thread, task, false);
    }

    private static void schedule(Thread thread, Runnable task, boolean isWatch) {
        pendingEntries.add(new Entry(thread, task, isWatch));

        if (started.compareAndSet(false, true)) {
            Thread watcherThread = threadFactory.newThread(watcher);
            watcherThread.start();
            ThreadDeadWatcher.watcherThread = watcherThread;
        }
    }


    private static final class Watcher implements Runnable {
        private final List<Entry> watchees = new ArrayList<>();
        @Override
        public void run() {
            for (; ; ) {
                fetchWatchees();
                notifyWatchees();

                fetchWatchees();
                notifyWatchees();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                      //Ignore e.printStackTrace();
                }

                if (watchees.isEmpty() && pendingEntries.isEmpty()) {
                    boolean stopped = started.compareAndSet(true, false);

                    if (pendingEntries.isEmpty()) {
                        break;
                    }

                    //没启动成功
                    if (!started.compareAndSet(false, true)) {
                        break;
                    }
                }
            }
        }

        private void fetchWatchees() {
            for (; ; ) {
                Entry e = pendingEntries.poll();
                if (e == null) {
                    break;
                }

                if (e.isWatch) {
                    watchees.add(e);
                } else {
                    watchees.remove(e);
                }
            }
        }

        private void notifyWatchees() {
            List<Entry> watchees = this.watchees;
            for(int i = 0; i < watchees.size();) {
                Entry e = watchees.get(i);
                if (!e.thread.isAlive()) {
                    watchees.remove(e);
                    try {
                        e.task.run();
                    } catch (Throwable throwable) {
                        logger.warn("thread death watcher task raised an exception:", throwable);
                    }
                } else {
                    i++;
                }
            }
        }
    }

    private static final class Entry  {
        final Thread thread;
        final Runnable task;
        final boolean isWatch;

        Entry(Thread thread, Runnable task, boolean isWatch) {
            this.thread = thread;
            this.task = task;
            this.isWatch = isWatch;
        }

        @Override
        public int hashCode() {
            return thread.hashCode() ^ task.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == this)
                return true;

            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry that = (Entry)obj;
            return thread == that.thread && task == that.task;
        }
    }

}

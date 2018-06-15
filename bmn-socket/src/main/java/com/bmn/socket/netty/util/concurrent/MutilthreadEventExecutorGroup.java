package com.bmn.socket.netty.util.concurrent;


import java.awt.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017/1/9.
 */
public abstract class MutilthreadEventExecutorGroup extends AbstractEventExecutorGroup {
    private final EventExecutor[] children;
    private final Set<EventExecutor> readonlyChildren;
    private final AtomicInteger terminatedChildren = new AtomicInteger();
    private final Promise<?> terminationFuture = new DefaultPromise<>(null);
    private final EventExecutorChooserFactory.EventExecutorChooser chooser;

    protected MutilthreadEventExecutorGroup(int nThreads, ThreadFactory threadFactory, Object... args) {
        this(nThreads, threadFactory == null ? null : new ThreadPerTaskExecutor(threadFactory), args);
    }

    protected MutilthreadEventExecutorGroup(int nThreads, Executor executor, Object... args) {
        this(nThreads, executor, DefaultEventExecutorChooserFactory.INSTNACE, args);
    }

    protected MutilthreadEventExecutorGroup(int nThreads, Executor executor, EventExecutorChooserFactory chooserFactory, Object... args) {
        if(nThreads <= 0) {
            throw new IllegalArgumentException(String.format("nThreads: %d (expected: > 0)", nThreads));
        }

        if(executor == null) {
            executor = null;
        }

        children = new EventExecutor[nThreads];

        for(int i = 0; i < nThreads; i++) {
            boolean success = false;
            try {
                children[i] = newChild(executor, args);
                success = true;
            } catch (Exception e) {
                throw new IllegalArgumentException("failed to create a child event loop", e);
            } finally {
                if(!success) {
                    for(int j = 0; j < i; j++) {
                        children[j].shutdownGracefully();
                    }

                    for(int j = 0; j < i; j++) {
                        EventExecutor e = children[j];
                        try {
                            while(!e.isTerminated()) {
                                e.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
                            }
                        } catch (InterruptedException r) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }
            }
        }

        chooser = chooserFactory.newChooser(children);

        final FutureListener<Object>  terminationListener = new FutureListener<Object>() {

            @Override
            public void operationComplete(Future<Object> future) throws Exception {
                if(terminatedChildren.incrementAndGet() == children.length) {
                    terminationFuture.setSuccess(null);
                }

            }
        };

        for(EventExecutor e : children) {
            e.terminationFuture().addListener(terminationListener);
        }

        Set<EventExecutor> childrenSet = new LinkedHashSet<>(children.length);
        Collections.addAll(childrenSet, children);
        readonlyChildren = Collections.unmodifiableSet(childrenSet);
    }

    protected ThreadFactory newDefaultThreadFactory() {
        return new DefaultThreadFactory(getClass());
    }

    protected abstract EventExecutor newChild(Executor executor, Object... args) throws Exception;

    @Override
    public void shutdown() {
        for(EventExecutor e: children) {
            e.shutdown();
        }
    }

    @Override
    public boolean isShutdown() {
        for(EventExecutor e: children) {
            if(!e.isShutdown()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isTerminated() {
        for(EventExecutor e : children) {
            if(!e.isTerminated())
                return false;
        }
        return true;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long deadline = System.nanoTime() + unit.toNanos(timeout);
        loop: for(EventExecutor e : children) {
            for(;;) {
                long timeLeft = deadline - System.nanoTime();
                if(timeLeft <= 0) {
                    break loop;
                }
                if(e.awaitTermination(timeLeft, TimeUnit.NANOSECONDS)) {
                    break;
                }
            }
        }
        return isTerminated();
    }

    @Override
    public boolean isShuttingDown() {
        for(EventExecutor e : children) {
            if(!e.isShuttingDown()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
        for(EventExecutor e : children) {
            e.shutdownGracefully(quietPeriod, timeout, unit);
        }

        return terminationFuture();
    }

    @Override
    public Future<?> terminationFuture() {
        return terminationFuture;
    }

    @Override
    public EventExecutor next() {
        return chooser.next();
    }

    @Override
    public Iterator<EventExecutor> iterator() {
        return readonlyChildren.iterator();
    }

    public final int executorCount() {return children.length;}
}

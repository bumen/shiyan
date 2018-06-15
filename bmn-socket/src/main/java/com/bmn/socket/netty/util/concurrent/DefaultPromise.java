package com.bmn.socket.netty.util.concurrent;

import com.bmn.socket.netty.util.internal.ThrowableUtil;

import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * Created by Administrator on 2017/1/5.
 */
public class DefaultPromise<V> extends AbstractFuture<V> implements Promise<V> {
    private static final AtomicReferenceFieldUpdater<DefaultPromise, Object> RESULT_UPDATER;

    private static final String SUCCESS = "SUCCESS";
    private static final String UNCANCELLABLE = "UNCANCELLABLE";
    private static final CauseHolder CANCELLATION_CAUSE_HOLDER = new CauseHolder(ThrowableUtil.unknownStackTrace(new CancellationException(), DefaultPromise.class, "cancedddl(.ddd..)"));

    private final EventExecutor executor;
    private volatile Object result;
    private Object listeners;
    private short waiters;
    private boolean notifyingListeners;

    public DefaultPromise(EventExecutor executor) {
        this.executor = executor;
    }

    protected DefaultPromise() {
        // only for subclasses
        executor = null;
    }

    @Override
    public Promise<V> setSuccess(V result) {
        if(this.setSuccess0(result)) {
            this.notifyListeners();
            return this;
        } else {
            throw new IllegalArgumentException("complete already: " + this);
        }
    }

    @Override
    public boolean trySuccess(V result) {
        if(this.setSuccess0(result)) {
            this.notifyListeners();
            return true;
        }
        return false;
    }

    @Override
    public Promise<V> setFailure(Throwable var1) {
        if(this.setFailure0(var1)) {
            this.notifyListeners();
            return this;
        }

        throw new IllegalStateException("complete already: " + this, var1);
    }

    @Override
    public boolean tryFailure(Throwable var1) {
        if(this.setFailure0(var1)) {
            this.notifyListeners();
            return true;
        }
        return false;
    }

    @Override
    public boolean setUncancellable() {
        if(RESULT_UPDATER.compareAndSet(this, null, UNCANCELLABLE)) {
            return true;
        }
        Object result = this.result;
        return !isDone0(result) || !isCancelled0(result);
    }

    @Override
    public Promise<V> addListener(GenericFutureListener<? extends Future<? super V>> var1) {
        synchronized (this) {
            this.addListener0(var1);
        }

        if(this.isDone()) {
            this.notifyListeners();
        }
        return this;
    }

    private void addListener0(GenericFutureListener<? extends Future<? super V>> listener) {
        if(this.listeners == null) {
            this.listeners = listener;
        } else if(this.listeners instanceof DefaultFutureListeners) {
            ((DefaultFutureListeners) this.listeners).add(listener);
        } else {
            this.listeners = new DefaultFutureListeners((GenericFutureListener)this.listeners, listener);
        }
    }

    private void removeListener0(GenericFutureListener<? extends Future<?super V>> listener) {
        if(this.listeners instanceof DefaultFutureListeners) {
            ((DefaultFutureListeners) this.listeners).remove(listener);
        } else if(this.listeners == listener) {
            this.listeners = null;
        }
    }

    @Override
    public Promise<V> addListeners(GenericFutureListener... var1) {
        synchronized (this) {
            GenericFutureListener[] arr$ = var1;
            int len$ = var1.length;
            int i$ = 0;
            while(i$ < len$) {
                GenericFutureListener listener = arr$[i$];
                if(listener != null) {
                    this.addListener0(listener);
                    ++i$;
                }
            }
        }

        if(this.isDone()) {
            this.notifyListeners();
        }
        return this;
    }

    @Override
    public Promise<V> removeListener(GenericFutureListener<? extends Future<? super V>> var1) {
        synchronized (this) {
            this.removeListener0(var1);
            return this;
        }
    }

    @Override
    public Promise<V> removeListeners(GenericFutureListener... var1) {
        synchronized (this) {
            GenericFutureListener[] arr$ = var1;
            int len$ = var1.length;
            for(int i$ = 0; i$ < len$; i$++) {
                GenericFutureListener l = arr$[i$];
                if(l == null)
                    break;
                this.removeListener0(l);
            }
            return this;
        }
    }

    @Override
    public Promise<V> await() throws InterruptedException {
        if(this.isDone()) {
            return this;
        } else if(Thread.interrupted()) {
            throw new InterruptedException(this.toString());
        } else {
            this.checkDeadLock();
            synchronized (this) {
                while(!this.isDone()) {
                    this.incWaiters();

                    try {
                        this.wait();
                    } finally {
                        this.decWaiters();
                    }
                }
                return this;
            }
        }
    }

    private void decWaiters() {
        --this.waiters;
    }

    private void incWaiters() {
        if(this.waiters == 32767) {
            throw new IllegalStateException("too many waiters: " + this);
        } else {
            ++this.waiters;
        }
    }

    protected void checkDeadLock() {
        EventExecutor e = executor();
        if(e != null && e.inEventLoop()) {
            throw new IllegalStateException("dead lock");
        }
    }

    @Override
    public Promise<V> awaitUninterruptibly() {
        if(this.isDone()) {
            return this;
        } else {
            this.checkDeadLock();
            boolean interrupted = false;
            synchronized (this) {
                while(!this.isDone()) {
                    this.incWaiters();

                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        interrupted = true;
                    } finally {
                        this.decWaiters();
                    }
                }
            }

            if(interrupted) {
                Thread.currentThread().interrupt();
            }
            return this;
        }
    }

    @Override
    public Promise<V> sync() throws InterruptedException {
        this.await();
        rethrowIfFailed();
        return this;
    }

    @Override
    public Promise<V> syncUninterruptibly() {
        this.awaitUninterruptibly();
        this.rethrowIfFailed();
        return this;
    }

    @Override
    public boolean isSuccess() {
        Object result = this.result;
        return result != null && result != UNCANCELLABLE && !(result instanceof DefaultPromise.CauseHolder);
    }

    @Override
    public boolean isCancellable() {
        return result == null;
    }

    @Override
    public Throwable cause() {
        Object result = this.result;
        return result instanceof DefaultPromise.CauseHolder ? ((CauseHolder) result).cause: null;
    }

    @Override
    public boolean await(long timeout, TimeUnit uint) throws InterruptedException {
        return this.await0(uint.toNanos(timeout), true);
    }

    @Override
    public boolean await(long timeoutMillis) throws InterruptedException {
        return this.await0(TimeUnit.MILLISECONDS.toNanos(timeoutMillis), true);
    }

    @Override
    public boolean awaitUninterruptibly(long timeout, TimeUnit uint) {
        try {
            return this.await0(uint.toNanos(timeout), false);
        } catch (InterruptedException var5) {
            throw new InternalError();
        }
    }

    @Override
    public boolean awaitUninterruptibly(long timeoutMillis) {
        try {
            return this.await0(TimeUnit.MILLISECONDS.toNanos(timeoutMillis), false);
        }catch (InterruptedException e) {
            throw new InternalError();
        }
    }

    @Override
    public V getNow() {
        Object result = this.result;
        return !(result instanceof CauseHolder) && result != SUCCESS ? (V)result : null;
    }

    @Override
    public boolean cancel(boolean var1) {
        if(RESULT_UPDATER.compareAndSet(this, null, CANCELLATION_CAUSE_HOLDER)) {
            this.checkNotifyWaiters();
            this.notifyListeners();
            return true;
        }
        return false;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled0(this.result);
    }

    @Override
    public boolean isDone() {
        return isDone0(this.result);
    }

    private void notifyListeners() {
        if(this.listeners != null) {
            this.notifyListenersWithStackOverFlowProtection();
        }
    }

    private void notifyListenersWithStackOverFlowProtection() {

    }

    protected  static void notifyListener(EventExecutor eventExecutor, Future<?> future, GenericFutureListener<?> listener) {

    }

    private static void notifyListenerWithStackOverFlowProtection(EventExecutor executor, final Future<?> future, final GenericFutureListener<?> listener) {
        if(executor.inEventLoop()) {

        }

        safeExecute(executor, new Runnable() {
            @Override
            public void run() {
                DefaultPromise.notifyListener0(future, listener);
            }
        });
    }

    private static void safeExecute(EventExecutor executor, Runnable task) {
        try {
            executor.execute(task);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void notifyListenersNow() {
        Object listeners;
        synchronized (this) {
            if(this.notifyingListeners || this.listeners == null) {
                return;
            }

            this.notifyingListeners = true;
            listeners = this.listeners;
            this.listeners = null;
        }

        while(true) {
            if(listeners instanceof DefaultFutureListeners) {
                this.notifyListeners0((DefaultFutureListeners) listeners);
            } else {
                notifyListener0(this, (GenericFutureListener) listeners);
            }

            synchronized (this) {
                if(this.listeners == null) {
                    this.notifyingListeners = false;
                    return;
                }

                listeners = this.listeners;
                this.listeners = null;
            }
        }
    }

    private void notifyListeners0(DefaultFutureListeners listeners) {
        GenericFutureListener[] a = listeners.listeners();
        int size = listeners.size();
        for(int i = 0; i < size; i++) {
            notifyListener0(this, a[i]);
        }
    }

    private static void notifyListener0(Future future, GenericFutureListener listener) {
        try {
            listener.operationComplete(future);
        }catch (Throwable e) {

        }
    }

    private boolean await0(long timeoutNanos, boolean interruptable) throws InterruptedException {
        if(this.isDone()) {
            return true;
        } else if(timeoutNanos <= 0L) {
            return this.isDone();
        } else if(interruptable && Thread.interrupted()) {
            throw new InterruptedException(this.toString());
        } else {
            this.checkDeadLock();
            long startTime = System.nanoTime();
            long waitTime = timeoutNanos;
            boolean interrupted = false;

            try {
                boolean var26;
                do {
                    DefaultPromise var9 = null;
                    synchronized (this) {
                        this.incWaiters();
                        try {
                            this.wait(waitTime / 1000000L, (int) (waitTime % 1000000L));
                        } catch (InterruptedException var22) {
                            if(interrupted) {
                                throw var22;
                            }
                            interrupted = true;
                        } finally {
                            this.decWaiters();
                        }
                    }

                    if(this.isDone()) {
                        var26 = true;
                        return var26;
                    }
                    waitTime = timeoutNanos - (System.nanoTime() - startTime);
                } while(waitTime > 0);
                var26 = this.isDone();
                return var26;
            } finally {
                if(interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void rethrowIfFailed()  {
        Throwable cause = this.cause();
        if(cause != null) {
            try {
                throw cause;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    private static boolean isDone0(Object result) {
        return result != null && result != UNCANCELLABLE;
    }

    private static boolean isCancelled0(Object result) {
        return result instanceof DefaultPromise.CauseHolder && ((CauseHolder)result).cause instanceof CancellationException;
    }

    private boolean setSuccess0(V result) {
        return this.setValue0(result == null?SUCCESS:result);
    }

    private boolean setFailure0(Throwable t) {
        return setValue0(new CauseHolder(t));
    }

    private boolean setValue0(Object objResult) {
        if(!RESULT_UPDATER.compareAndSet(this, null, objResult) && !RESULT_UPDATER.compareAndSet(this, UNCANCELLABLE, objResult)) {
            return false;
        } else {
            checkNotifyWaiters();
            return true;
        }
    }

    private synchronized void checkNotifyWaiters() {
        if(this.waiters > 0) {
            this.notifyAll();
        }

    }

    protected EventExecutor executor() {
        return executor;
    }



    static {
        RESULT_UPDATER = AtomicReferenceFieldUpdater.newUpdater(DefaultPromise.class, Object.class, "result");
    }

    private static final class CauseHolder {
        final Throwable cause;

        CauseHolder(Throwable cause) {
            this.cause = cause;
        }
    }
}

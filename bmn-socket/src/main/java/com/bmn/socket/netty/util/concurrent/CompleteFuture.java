package com.bmn.socket.netty.util.concurrent;

import org.omg.CORBA.INITIALIZE;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/1/9.
 */
public abstract class CompleteFuture<V> extends AbstractFuture<V> {
    private final EventExecutor executor;

    protected CompleteFuture(EventExecutor executor) {
        this.executor = executor;
    }

    protected EventExecutor executor() {return this.executor;}

    public Future<V> addListener(GenericFutureListener<? extends Future<? super V>> listener) {
        if(listener == null) {
            throw new NullPointerException("listener");
        } else {
            DefaultPromise.notifyListener(this.executor(), this, listener);
            return this;
        }
    }

    public Future<V> addListeners(GenericFutureListener... listeners) {
        if(listeners == null) {
            throw new NullPointerException("listeners");
        } else {
            GenericFutureListener[] arr$ = listeners;
            int len$ = listeners.length;

            for(int i$ = 0; i$ < len$; i$++) {
                GenericFutureListener l = arr$[i$];
                if(l == null)
                    break;

                DefaultPromise.notifyListener(this.executor(), this, l);
            }
            return this;
        }
    }

    public Future<V> removeListener(GenericFutureListener<? extends Future<? super V>> listener) {return this;}

    public Future<V> removeListeners(GenericFutureListener... listeners) {
        return this;
    }

    public Future<V> await() throws InterruptedException {
        if(Thread.interrupted()) {
            throw new InterruptedException();
        } else {
            return this;
        }
    }

    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        if(Thread.interrupted()) {
            throw new InterruptedException();
        } else {
            return true;
        }
    }

    public Future<V> sync() throws InterruptedException {
        return this;
    }

    public Future<V> syncUninterruptibly() {
        return this;
    }

    public boolean await(long timeoutMills) throws InterruptedException {
        if(Thread.interrupted()) {
            throw new InterruptedException();
        } else {
            return true;
        }
    }

    public Future<V> awaitUninterruptibly() {return this;}

    public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
        return true;
    }

    public boolean awaitUninterruptibly(long timeoutMills) {return true;}

    public boolean isDone(){return true;}

    public boolean isCancellable(){return false;}

    public boolean isCancelled() {return false;}

    public boolean cancel(boolean mayInterruptIfRunning){return false;}


}

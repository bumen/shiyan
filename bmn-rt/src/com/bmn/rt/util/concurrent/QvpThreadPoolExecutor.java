package com.bmn.rt.util.concurrent;

import java.util.HashSet;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2017/10/18.
 */
public class QvpThreadPoolExecutor {

    private static class Tester {
        public static void main(String[] args) {
            QvpThreadPoolExecutor executor = new QvpThreadPoolExecutor(0, 1, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), null, null);
            System.out.println(Integer.toBinaryString(-1 << 29));
        }
    }


    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits
    /**
     * RUNNING:  能接受新提交的任务，并处理阻塞队列中任务。
     * SHUTDOWN: 不接受新提交的任务，但可以处理阻塞队列中任务。当RUNNING状态时，执行shutdown方法后线程池进入SHUTDOWN状态。（用户优雅关闭）
     * STOP:　   不接受新提交的任务，同时不处理阻塞队列中任务，并且会中断正在处理中的任务。当RUNNING或SHUTDOWN状态时，调用shutdownNow方法后线程池进入STOP状态。（用户强行关闭）
     * TIDYING:  所有任务都已终止，线程池中线程数量为0。
     *           当线程池为TIDYING时，会调用terminated方法后线程池进入TERMINATED状态。
     *           当线程池为SHUTDOWN时，如果线程池中没有线程了，并且阻塞队列为空，则线程池进入TERMAINTED状态。
     *           当线程池为STOP时，如果线程池中没有线程了，则线程池进入TERMINATED状态。
     * TERMINATED：terminated()方法调用完，进入该状态。
     *
     */
    private static final int RUNNING    = -1 << COUNT_BITS;     //111
    private static final int SHUTDOWN   =  0 << COUNT_BITS;     //000
    private static final int STOP       =  1 << COUNT_BITS;     //001
    private static final int TIDYING    =  2 << COUNT_BITS;     //010
    private static final int TERMINATED =  3 << COUNT_BITS;     //011

    // Packing and unpacking ctl
    private static int runStateOf(int c)     { return c & ~CAPACITY; }
    private static int workerCountOf(int c)  { return c & CAPACITY; }
    private static int ctlOf(int rs, int wc) { return rs | wc; }

    private void decrementWorkerCount() {
        do {} while (! compareAndDecrementWorkerCount(ctl.get()));
    }

    private boolean compareAndDecrementWorkerCount(int expect) {
        return ctl.compareAndSet(expect, expect - 1);
    }


    private final BlockingQueue<Runnable> workQueue;

    private volatile long keepAliveTime;

    private volatile boolean allowCoreThreadTimeOut;

    private volatile int corePoolSize;

    private volatile int maximumPoolSize;

    private volatile ThreadFactory threadFactory;

    private volatile RejectedExecutionHandler handler;

    public QvpThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit timeUnit, BlockingQueue<Runnable> workQueue,
                                 ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = timeUnit.toNanos(keepAliveTime);
        this.workQueue = workQueue;
        this.threadFactory = threadFactory;
        this.handler = rejectedExecutionHandler;
    }

    private final class Worker
            extends AbstractQueuedSynchronizer
            implements Runnable
    {
        /**
         * This class will never be serialized, but we provide a
         * serialVersionUID to suppress a javac warning.
         */
        private static final long serialVersionUID = 6138294804551838833L;

        /** Thread this worker is running in.  Null if factory fails. */
        final Thread thread;
        /** Initial task to run.  Possibly null. */
        Runnable firstTask;
        /** Per-thread task counter */
        volatile long completedTasks;

        /**
         * Creates with given first task and thread from ThreadFactory.
         * @param firstTask the first task (null if none)
         */
        Worker(Runnable firstTask) {
            setState(-1); // inhibit interrupts until runWorker
            this.firstTask = firstTask;
            this.thread = getThreadFactory().newThread(this);
        }

        /** Delegates main run loop to outer runWorker  */
        public void run() {
            runWorker(this);
        }

        // Lock methods
        //
        // The value 0 represents the unlocked state.
        // The value 1 represents the locked state.

        protected boolean isHeldExclusively() {
            return getState() != 0;
        }

        protected boolean tryAcquire(int unused) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        protected boolean tryRelease(int unused) {
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        public void lock()        { acquire(1); }
        public boolean tryLock()  { return tryAcquire(1); }
        public void unlock()      { release(1); }
        public boolean isLocked() { return isHeldExclusively(); }

        void interruptIfStarted() {
            Thread t;
            if (getState() >= 0 && (t = thread) != null && !t.isInterrupted()) {
                try {
                    t.interrupt();
                } catch (SecurityException ignore) {
                }
            }
        }
    }

    public ThreadFactory getThreadFactory() {
        return threadFactory;
    }


    /**
     * 获取阻塞队列中的任务
     * @return
     */
    private Runnable getTask() {
        boolean timedOut = false; // Did the last poll() time out?

        for (;;) {
            int c = ctl.get();
            int rs = runStateOf(c);

            // Check if queue empty only if necessary.
            /**
             * 1. 判断线程池的状态
             * 此时线程池不是RUNNING状态
             * 当是SHTUDOWN时， 如果阻塞队列为空，则关闭线程。
             * 当是STOP时，直接关闭线程
             */
            if (rs >= SHUTDOWN && (rs >= STOP || workQueue.isEmpty())) {
                decrementWorkerCount();
                return null;
            }

            int wc = workerCountOf(c);

            // Are workers subject to culling?
            boolean timed = allowCoreThreadTimeOut || wc > corePoolSize;

            /**
             * 2. 判断大于corePoolSize
             * 此时线程池是RUNNING状态
             * 作用：在RUNNING状态下，通过超时来调整线程池中线程线程数量 。确保线程数在corePoolSize范围内
             * 超时：通过判断阻塞队列中是否有任务。
             */
            if ((wc > maximumPoolSize || (timed && timedOut))
                    && (wc > 1 || workQueue.isEmpty())) {
                if (compareAndDecrementWorkerCount(c))
                    return null;
                continue;
            }

            try {
                /**
                 * 1. 如果是超时线程，则阻塞keepAliveTime，在keepAliveTime内没有获取到任务，则关闭线程。
                 * 2. 如果不是超时线程，则一直阻塞获取任务，该线程一直存在
                 */
                Runnable r = timed ?
                        workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) :
                        workQueue.take();
                if (r != null)
                    return r;
                timedOut = true;
            } catch (InterruptedException retry) {
                timedOut = false;
            }
        }
    }

    private static boolean runStateLessThan(int c, int s) {
        return c < s;
    }

    private static boolean runStateAtLeast(int c, int s) {
        return c >= s;
    }


    /**
     * 线程执行任务
     * @param w
     */
    final void runWorker(Worker w) {
        Thread wt = Thread.currentThread();
        Runnable task = w.firstTask;
        w.firstTask = null;
        //使得线程执行前，能得到关闭成为可能。 可以tryLock()return true;
        w.unlock(); // allow interrupts
        boolean completedAbruptly = true;
        try {
            //线程一直执行，直到没有任务
            while (task != null || (task = getTask()) != null) {
                w.lock();
                // If pool is stopping, ensure thread is interrupted;
                // if not, ensure thread is not interrupted.  This
                // requires a recheck in second case to deal with
                // shutdownNow race while clearing interrupt
                if ((runStateAtLeast(ctl.get(), STOP) ||
                        (Thread.interrupted() &&
                                runStateAtLeast(ctl.get(), STOP))) &&
                        !wt.isInterrupted())
                    wt.interrupt();
                try {
                    beforeExecute(wt, task);
                    Throwable thrown = null;
                    try {
                        task.run();
                    } catch (RuntimeException x) {
                        thrown = x; throw x;
                    } catch (Error x) {
                        thrown = x; throw x;
                    } catch (Throwable x) {
                        thrown = x; throw new Error(x);
                    } finally {
                        afterExecute(task, thrown);
                    }
                } finally {
                    task = null;
                    w.completedTasks++;
                    w.unlock();
                }
            }
            completedAbruptly = false;
        } finally {
            processWorkerExit(w, completedAbruptly);
        }
    }

    private final HashSet<Worker> workers = new HashSet<Worker>();
    private final ReentrantLock mainLock = new ReentrantLock();
    private long completedTaskCount;

    private void processWorkerExit(Worker w, boolean completedAbruptly) {
        if (completedAbruptly) // If abrupt, then workerCount wasn't adjusted
            decrementWorkerCount();

        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            completedTaskCount += w.completedTasks;
            workers.remove(w);
        } finally {
            mainLock.unlock();
        }

        tryTerminate();

        int c = ctl.get();
        if (runStateLessThan(c, STOP)) {
            if (!completedAbruptly) {
                int min = allowCoreThreadTimeOut ? 0 : corePoolSize;
                if (min == 0 && ! workQueue.isEmpty())
                    min = 1;
                if (workerCountOf(c) >= min)
                    return; // replacement not needed
            }
            addWorker(null, false);
        }
    }

    private void addWorker(Object o, boolean b) {

    }

    private static boolean isRunning(int c) {
        return c < SHUTDOWN;
    }

    private static final boolean ONLY_ONE = true;

    private void tryTerminate() {
        for (;;) {
            int c = ctl.get();
            if (isRunning(c) ||
                    runStateAtLeast(c, TIDYING) ||
                    (runStateOf(c) == SHUTDOWN && ! workQueue.isEmpty()))
                return;
            /**
             * 1. SHUTDOWN状态，且workqueue为空
             * 2. STOP状态
             */
            if (workerCountOf(c) != 0) { // Eligible to terminate
                interruptIdleWorkers(ONLY_ONE);
                return;
            }

            /**
             * 线程数为0且，阻塞队列为空。则terminate
             */
            final ReentrantLock mainLock = this.mainLock;
            mainLock.lock();
            try {
                if (ctl.compareAndSet(c, ctlOf(TIDYING, 0))) {
                    try {
                        terminated();
                    } finally {
                        ctl.set(ctlOf(TERMINATED, 0));
                        termination.signalAll();
                    }
                    return;
                }
            } finally {
                mainLock.unlock();
            }
            // else retry on failed CAS
        }
    }

    private final Condition termination = mainLock.newCondition();
    private void interruptIdleWorkers() {
        interruptIdleWorkers(false);
    }

    private void interruptIdleWorkers(boolean onlyOne) {
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            for (Worker w : workers) {
                Thread t = w.thread;
                /**
                 * 可以中断，还未开始，与正在阻塞获取任务的线程
                 */
                if (!t.isInterrupted() && w.tryLock()) {
                    try {
                        t.interrupt();
                    } catch (SecurityException ignore) {
                    } finally {
                        w.unlock();
                    }
                }
                if (onlyOne)
                    break;
            }
        } finally {
            mainLock.unlock();
        }
    }


    private void terminated() {

    }


    protected void beforeExecute(Thread wt, Runnable task) {

    }
    protected void afterExecute(Runnable r, Throwable t) { }
}

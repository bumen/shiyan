package com.bmn.concurrent;

/**
 * <pre>
 *     读写锁：读读， 可以读写，写写。
 *
 * </pre>
 * unlockRead, unlockWrite都调用了notifyAll, 而不是notify
 * 如果有线程再获取读锁，同时又有线程在等待获取写锁。如果这时其中一个等待读锁的线程被notify唤醒，但因为此时仍有请求写锁的线程在writeRequest>0，
 * 所以被唤醒的线程会再次进入阻塞状态。然而，等待写锁的线程一个也没被唤醒，就像什么也没有发生过一样（信号丢失）。
 * 如果使用notifyAll， 所有线程都会被唤醒，然后判断是否获得其请求的锁。
 *
 * <p>
 *    1. 不可重入的读写锁
 *    2. 读操作不可被写操作打断，可能会出再饥饿线程（不停的读，而一直等待写）
 * </p>
 *
 */
public class ReadWriteLock {



    private int readers = 0;
    private int writers = 0;
    private int writeRequest = 0;

    public synchronized void lockRead() throws InterruptedException {
        while(writers > 0 || writeRequest > 0) {
            this.wait();
        }

        this.readers++;
    }

    public  synchronized void unlockRead() {
        readers--;
        this.notifyAll();
    }

    public synchronized void lockWrite() throws InterruptedException {
        writeRequest++;
        while(writers > 0 || readers > 0) {
            this.wait();
        }

        this.writeRequest--;
        this.writers++;
    }

    public synchronized void unlockWrite() {
        this.writers--;
        this.notifyAll();
    }
}

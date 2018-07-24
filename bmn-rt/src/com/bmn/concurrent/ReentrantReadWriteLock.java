package com.bmn.concurrent;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     可重入的读写锁
 *    1. 读锁重入，如果没有读满足没写或写请求， 如果有读锁不管是否有写请求
 *    2. 写锁重入，只有写锁，才能再重入
 *    3. 读锁升级到写锁：有读锁的时候可以重入写锁，要求只有这个线程是唯一个拥有读锁的线程。
 *    4. 写锁降级到读锁：有写锁的时候可以重入读锁，
 * </p>
 */
public class ReentrantReadWriteLock {

    /***************读锁重入********************/

    private Map<Thread, Integer> readingThreads = new HashMap<>();

    private int writers = 0;
    private int writeRequest = 0;

    public synchronized void lockRead() throws InterruptedException {
        Thread callingRead = Thread.currentThread();
        while (!canGrantReadAccess(callingRead)) {
            this.wait();
        }
        readingThreads.put(callingRead, getReadAccessCount(callingRead) + 1);
    }

    public synchronized void unlockRead() {
        Thread callingThread = Thread.currentThread();
        int accessCount = getReadAccessCount(callingThread);
        if(accessCount == 0) {
            return;
        }

        if (accessCount == 1) {
            readingThreads.remove(callingThread);
        } else {
            readingThreads.put(callingThread, accessCount - 1);
        }
    }

    private boolean canGrantReadAccess(Thread callingThread) {
        //有写锁，则不可以读
        if(writers > 0) return false;
        //没有写锁，有读锁，则不管是否有写请求。则可以重入
        if(isReader(callingThread)) return true;
        //没有读锁，有写请求。则不可以获取锁
        if(writeRequest > 0) return false;

        return true;
    }

    private int getReadAccessCount(Thread callingThread){
        Integer accessCount = readingThreads.get(callingThread);
        if(accessCount == null) return 0;
        return accessCount.intValue();
    }


    private boolean isReader(Thread callingThread) {
        return readingThreads.get(callingThread) != null;
    }

    /****************写锁重入******************/


    private Thread writingThread = null;

    public synchronized void lockWirte() {
        Thread callingThread = Thread.currentThread();
        writeRequest++;


    }
}

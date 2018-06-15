package com.bmn.http.httpclient.impl.server;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/5/24.
 */
public class WorkerPoolExecutor extends ThreadPoolExecutor {
    private final Map<Worker, Boolean> workerSet;


    public WorkerPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                              TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        this.workerSet = new ConcurrentHashMap<>();
    }


    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        if(r instanceof Worker) {
            this.workerSet.put((Worker)r, Boolean.TRUE);
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        if(r instanceof Worker) {
            this.workerSet.remove(r);
        }
    }

    public Set<Worker> getWorkers() {
        return new HashSet<Worker>(this.workerSet.keySet());
    }
}

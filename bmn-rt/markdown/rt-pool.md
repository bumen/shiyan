## java 线程池

### ThreadPoolExecutor
 * newSingleThreadExecutor
 * newFixedThreadPool
 
### newFixedThreadPool 与 newSingleThreadExecutor
 * fix 返回的是Pool， single返回的是DelegatedExecutor
   + pool 可以设置大小，single不可以设置大小。只有ExecutorService接口功能
   
   
   
### Scheduled
 * fixDelay
 * fixRate
 ``` 
    if (periodNanos == 0) {
        if (setUncancellableInternal()) {
            V result = task.call();
            setSuccessInternal(result);
        }
    } else {
        // check if is done as it may was cancelled
        if (!isCancelled()) {
            task.call();  // 先执行任务
            if (!executor().isShutdown()) {
                long p = periodNanos;
                if (p > 0) {
                    deadlineNanos += p;  // fixRate处理
                } else {
                    deadlineNanos = nanoTime() - p; // fixDelay处理
                }
                if (!isCancelled()) {
                    // scheduledTaskQueue can never be null as we lazy init it before submit the task!
                    Queue<ScheduledFutureTask<?>> scheduledTaskQueue =
                            ((AbstractScheduledEventExecutor) executor()).scheduledTaskQueue;
                    assert scheduledTaskQueue != null;
                    scheduledTaskQueue.add(this);
                }
            }
        }
    }
 ```
   
   
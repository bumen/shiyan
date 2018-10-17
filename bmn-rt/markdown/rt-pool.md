## java 线程池

### ThreadPoolExecutor
 * newSingleThreadExecutor
 * newFixedThreadPool
 
### newFixedThreadPool 与 newSingleThreadExecutor
 * fix 返回的是Pool， single返回的是DelegatedExecutor
   + pool 可以设置大小，single不可以设置大小。只有ExecutorService接口功能
   
   
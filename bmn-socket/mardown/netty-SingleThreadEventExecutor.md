## SingleThreadEventExecutor分析

### 属性
 * 作用为一个线程执行器，需要具备哪些属性
 * interrupted：作用是在线程启动前保存中断标识，当启动时来通知线程中断
 * 任务队列 
 * 执行线程队象
 * 执行器状态
 * shutdownHook
 * 饱和策略
 * 线程工厂
 * 几个控制shutdown属性
 * 全局锁
 * terminationfuture
 
### shutdownGracefully
 * 只是设置ST_SHUTTING_DOWN状态。如果没有启动，则启动。否则添加唤醒任务

### confirmShutdown
 * 对关闭操作进行确认

### execute
 * 执行执行任务。
 * 把任务放到队列，并启动线程执行
 * shutdown状态不接受任务。shuttingdown时还可以接受任务

### doStartThread
 * 启动线程执行run
 * 处理线程结束流程
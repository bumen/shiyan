## jmm-thread 线程安全
 * https://blog.csdn.net/javazejian/article/details/72772461

### synchronized
 * 同步方法
   + 通过方法ACC_SYNCHRONIZED标记实现
   + 方法执行过程中，发生了异常，并且方法内部并没有处理该异常，那么在异常被抛到方法外面之前监视器锁会被自动释放。
 * 同步代码块
   + 通过monitorenter, monitorexit指令码实现
   
   
### 上下文切换
 * 引起上下文切换的原因
   + 当前执行任务的时间片用完之后, 系统CPU正常调度下一个任务 
   + 当前执行任务碰到IO阻塞, 调度器将挂起此任务, 继续下一任务
   + 多个任务抢占锁资源, 当前任务没有抢到,被调度器挂起, 继续下一任务
   + 用户代码挂起当前任务, 让出CPU时间
   + 硬件中断
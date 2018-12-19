## jmm-thread 线程安全
 * https://blog.csdn.net/javazejian/article/details/72772461

### synchronized
 * 同步方法
   + 通过方法ACC_SYNCHRONIZED标记实现
   + 方法执行过程中，发生了异常，并且方法内部并没有处理该异常，那么在异常被抛到方法外面之前监视器锁会被自动释放。
 * 同步代码块
   + 通过monitorenter, monitorexit指令码实现
   
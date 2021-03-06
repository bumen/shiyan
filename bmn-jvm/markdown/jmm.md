## JMM java内存模型
 * 为了保证共享内存的正确性（可见性、有序性、原子性），内存模型定义了共享内存系统中多线程程序读写操作行为的规范。
 * 内存模型解决并发问题主要采用两种方式：限制处理器优化和使用内存屏障
 
### 并发编程
 * 并发编程，为了保证数据的安全，需要满足以下三个特性：
   + 原子性是指在一个操作中就是cpu不可以在中途暂停然后再调度，既不被中断操作，要不执行完成，要不就不执行。
   + 可见性是指当多个线程访问同一个变量时，一个线程修改了这个变量的值，其他线程能够立即看得到修改的值。
   + 有序性即程序执行的顺序按照代码的先后顺序执行。
 * 缓存一致性问题其实就是可见性问题
 * 处理器优化是可以导致原子性问题的
 * 指令重排即会导致有序性问题

### java jmm对原子性，顺序性，可见性保证
 * 原子性：
   + 方法级别与代码块级别原子性，通过锁保证
   + 原子变量
 * 可见性：
   + 锁，与volatile
 * 有序性：
   + voaltile，可以禁止重排序
   
 * happen-before规则，可以保证原子性，可见性，有序性

### happen-before规则
 * 
 
### volatile内存语义
 * 可见性
 * 禁止指令重排序 
   + 通过内存屏蔽指令
 * 正确使用
   + 对变量的写操作不依赖于当前值。
   + 该变量没有包含在具有其他变量的不变式中。
 
#### volatile使用 
 * 一个volatile变量，有不同的状态值，且这些状态值是有顺序的。可以实现多线程互斥操作。
 * 要么状态有序，要么是消息有序。

### 指令重排序
 * 如果两个条指令不存数据依赖关系而且无论重排前还是重排后程序的执行结果在单线程中并没有改变，因此这种重排优化是允许的。
   + 数据依赖，对同一变量就是读写操作
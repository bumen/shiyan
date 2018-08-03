## java基础面试

### jdk
#### 运算符优先级
 * (t != (t = tail)) ? t : head;
 * tail = tail.next = node;
 
#### 数据类型

#### 类加载器

#### 泛型

#### 注解

#### 反射

#### io, nio

#### 普通集合
 * HashMap
   + key, value是否为null
   + size, isEmpty实现
   + 数据结构
   + hash算法
   + 扩容
 * LinkedHashMap
 * TreeMap
 * WeakHashMap
 * ArrayList
 * LinedList

#### 多线程编程
 * Thread类
   + 状态转换图
   + 中断
   
 * 原子变量
 * ThreadLocal
 * 并发集合
 * 锁
 * 线程池
 
 * 读写与线程池使用一个变量表示两个属性，通过compare-set实现, 两个属性互斥
   
#### 锁
 * AQS
 * ReentrantLock
 * 读写锁
 * 信号量
 * 闭锁
 
#### 并发集合
 * ConcurrentHashMap
   + key, value是否为null
   + size, isEmpty实现
   + 数据结构
   + hash算法
   + 扩容
 * ConcurrentLinkedQueue
   + 数据结构
   + size, isEmpty实现
   + offer, poll实现原理
   + dummy node
   + 弱一致性
 * LinkedBlockingQueue
   + 阻塞
   + 实现原理
   + size, isEmpty实现
   + dummy node
   
 * ArrayBlockingQueue
 
 & SynchronizedQueue
 
#### 线程池
 * 构造函数
 * 几种状态，转换图
 * 为什么使用一个变量来表示状态与线程数两种属性

### jvm

#### 字节码

#### 垃圾回收

#### 编译器

#### 类加载过程

#### 调优

### jmm

### java安全

### java网络
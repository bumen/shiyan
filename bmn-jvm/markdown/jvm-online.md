## JVM线上调优

### gc
 * 配置 
   + 8核
   + 16G
   + jdk8
 * jvm 参数
   + -xx:NewSize=2791m -XX:MaxNewSize=2791m
   + -Xms11164m -Xmx11164m
   + -XX:+UseConcMarkSweepGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70
      - 老年代百分70回收
   + 采用新生代默认占比 s0 = s1 = 2791*0.1= 279.1m
      Eden = 2791 * 0.8 = 2.2m
   + Old大小：11164 - 2791 = 8373
   + 对象在新生代存活的次数：是6
   
 * 正常gc标准
   + YGC 3s+一次
   + OGC 10分钟以上一次
   
 * cpu占比不高300-400%
   + 每5分钟有一个存数据操作数据比较大时。这个线程比较占cpu大概96%左右
   + 导致这个业务线程慢。会造成由这个业务线程处理业务时会有卡顿
   
 * 内存稳定
   + jstat -gcutil pid 3000
   + YGC：3-9秒一次
   + FGC: 11分钟一次 
     - 在70%时会FGC一次
     
### 线上业务卡问题排查
 * 可能原因
   + fullgc频繁
   + cpu比较高
   + cpu不高，可能是业务线程被block, 通常是异步获取锁
   
 * 如果是fullgc频繁，可能是内存泄漏
 * 如果是cpu比较高，可能是业务线程问题。死循环
 
#### 使用logback异步写日志造成卡顿问题
 * 首先cpu正常值，30-40，然后发现业务卡，怀疑逻辑线程被block住了。
 * 然后top -Hp pid 查看占cpu高的线程
 * 发现
 ``` 
  "AsyncAppender-Worker-ASYNC-SIFT" #1180 daemon prio=5 os_prio=0 tid=0x00007fc5f0007800 nid=0x2015 runnable [0x00007fc6720b8000]
     java.lang.Thread.State: WAITING (parking)
          at sun.misc.Unsafe.park(Native Method)
          - parking to wait for  <0x00000005b887c460> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
          at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
          at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
          at java.util.concurrent.ArrayBlockingQueue.take(ArrayBlockingQueue.java:403)
          at ch.qos.logback.core.AsyncAppenderBase$Worker.run(AsyncAppenderBase.java:289)
 ```
 * 发现了logback这个异步线程，一是使用了ArrayBlockingQueue，而是发现在take，
 说明之前一直在快速的处理日志队列消息，然后队列为空，在等待消息
 * 那么直接怀疑是否是因为逻辑线程向logback异步日志，写到ArrayBlockingQueue。
 而ArrayBlockingQueue的入队和出队共用一把锁，入队和出队存在竞争，一方速度高时另一方速度会变低,
 即逻辑线程在向logback的异步队列写日志的时候，获取锁的时候被阻塞了一下。
 * 解决方案使用linkedBlockQueue
   + 一般要求用有界队列，防止生产者生产过快导致内存溢出
   + 所这种不是最终方案
 * 使用log4j2环形无锁队列 
 
 
### 内存泄漏排查
 * 现象：fullgc频繁
 * 排查
   + jmap -heap:live pid 查看哪个对象创建比较多
      - 如果线上内存泄漏那测试环境也会有，因为快照会暂停jvm。所以尽量不要在线上dump
      - 在测试环境上查看
      - dump 测试环境上内存镜像
   + jmap -dump:live,format=b,file=xxx.bin pid
   + 下载快照文件
   + mat工具分析
   + 找到缓存对象的位置。debug断点调试，查看堆栈信息，定位出问题调用
   
#### ThreadLocal内存泄漏
 * 发现fgc次数很多而且频繁，此时老年代占比已经大约70%左右，且已经回收不了内存，我们这边设置的fgc阈值是老年代的70%。
 此时因为还有30%的老年空间，所以整体内存相对还算稳定，CPU也比较稳定，但是有很大的潜在的风险，就是内存一直上涨，不释放。
 * jstat -gcutil pid 5000
   ``` 
     S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT   
      0.00  55.09  88.41  72.10  92.64  85.22   9223 1169.442   435  168.866 1338.307
     57.54   0.00  82.24  72.31  92.64  85.22   9224 1169.542   436  168.877 1338.418
      0.00  63.75   5.33  72.50  92.64  85.22   9225 1169.642   436  168.877 1338.519
      0.00  63.75  34.02  72.50  92.64  85.22   9225 1169.642   436  168.877 1338.519
      0.00  63.75  59.26  72.50  92.64  85.22   9225 1169.642   436  168.877 1338.519
      0.00  63.75  81.37  72.50  92.64  85.22   9225 1169.642   436  168.877 1338.519
     55.60   0.00  11.75  72.71  92.64  85.22   9226 1169.742   436  168.877 1338.619
     55.60   0.00  40.07  72.71  92.64  85.22   9226 1169.742   436  168.877 1338.619
     55.60   0.00  67.86  72.70  92.64  85.22   9226 1169.742   437  169.541 1339.284
      0.00  56.04   4.21  72.59  92.64  85.22   9227 1169.838   437  169.541 1339.379
      0.00  56.04  30.01  71.73  92.64  85.22   9227 1169.838   438  169.649 1339.487
      0.00  56.04  57.75  71.73  92.64  85.22   9227 1169.838   438  169.649 1339.487
      0.00  56.04  79.01  71.73  92.64  85.22   9227 1169.838   438  169.649 1339.487
     55.39   0.00   2.54  71.92  92.64  85.22   9228 1169.988   438  169.649 1339.638
     55.39   0.00  24.70  71.92  92.64  85.22   9228 1169.988   438  169.649 1339.638
     55.39   0.00  47.89  71.92  92.64  85.22   9228 1169.988   438  169.649 1339.638
     55.39   0.00  82.01  71.89  92.64  85.22   9228 1169.988   439  170.207 1340.196
   ```
 * 大概30多秒就会一次FGC
 * 初步猜测是出现了内存泄露
 * 通过jmap -histo/-histo:live pid >> log。导出fgc前后的histo对比，发现了一个实例数很大的对象。而且一直在增加
 * 排查
   + 查看相关业务代码
 * 查看实例对象被谁引入着
   >查这个问题的话，必须要把内存堆快照dump出来，然后利用工具检查，如mat
   >但是线上人很多，而且堆内存很多，导出一次很花费时间，会stw
 * 从测试服器导出快照
   > jmap -dump:live,format=b,file=pid_heap.bin pid，导出开发服务器java进程的堆内存快照
   > 直接利用mat打开
 * 对mat使用有经验的话，操作步骤是直接
   > 选择dominator_tree
   > 搜索实例对象
   > List Objects
   > 选择With incoming references，谁持有了它的引用
 * 直接发现是被ThreadLocal引用
   ```
   | Class Name                                                                                    | Shallow Heap | Retained Heap
     -----------------------------------------------------------------------------------------------------------------------------
     <class> com.busniss.UseInfo @ 0x86abf6e0|           40 |            40
     '- [6890] java.lang.Object[65536] @ 0x868d3990                                                |      262,160 |     2,409,032
        '- table java.util.IdentityHashMap @ 0x86671628                                            |           40 |     2,409,072
           '- value java.lang.ThreadLocal$ThreadLocalMap$Entry @ 0x86671608                        |           32 |     2,409,104
              '- [29] java.lang.ThreadLocal$ThreadLocalMap$Entry[64] @ 0x866710f0                  |          272 |     2,416,616
                 '- table java.lang.ThreadLocal$ThreadLocalMap @ 0x8104eec0                        |           24 |     2,416,640
                    '- threadLocals java.lang.Thread @ 0x8104ecc8  queue-executor-handler-5 Thread |          120 |     2,417,008
     -----------------------------------------------------------------------------------------------------------------------------
   ```
   + queue-executor-handler-5是业务线程
   + value java.lang.ThreadLocal$ThreadLocalMap$Entry：ThreadLocal的map.entry.value是一个IdentityHashMap
      - 代码定义：ThreadLocal<IdentityHashMap<AugmentedBean, Set<String>>> names = new ThreadLocal<>()
      - 调用names.set(new IdentityHashMap<>())
   + table java.util.IdentityHashMap
      - 它的数据结构就是一个数组
      - [6890] java.lang.Object[65536] 大不是65536，使用了6890
   + Retained Heap
      - 占用大小比较大
      
 * 排查代码什么时候将UserInfo放到ThreadLocal
   + 首先定位业务。是一个每分钟的定时任务，会查所有在线用户信息，就创建新的UserInfo
   + 通过在业务代码加断点在ThreadLocal，set时加断点。然后查看堆栈信息
   
 * 线上临时解决
   + 通过beanshell，执行ThreadLocal.clean()
   ``` 
      74.73   0.00  16.02  72.48  92.61  85.04  10156 1313.117   575  271.355 1584.472
      74.73   0.00  34.71  72.48  92.61  85.04  10156 1313.117   575  271.355 1584.472
      74.73   0.00  54.42  72.48  92.61  85.04  10156 1313.117   575  271.355 1584.472
      74.73   0.00  73.29  72.48  92.61  85.04  10156 1313.117   575  271.355 1584.472
      74.73   0.00  89.41  72.48  92.61  85.04  10156 1313.117   575  271.355 1584.472
       0.00  71.54   9.25  72.74  92.64  85.06  10157 1313.303   576  272.188 1585.492
       0.00  71.54  28.30  72.73  92.64  85.06  10157 1313.303   577  272.188 1585.492
       0.00  71.54  55.85  72.73  92.64  85.06  10157 1313.303   577  272.463 1585.766
       0.00  71.54  78.05  72.73  92.64  85.06  10157 1313.303   577  272.463 1585.766
      69.21   0.00   1.70  70.98  92.64  85.06  10158 1313.438   578  273.320 1586.758
      69.21   0.00  19.97  63.09  92.64  85.06  10158 1313.438   578  273.320 1586.758
      69.21   0.00  39.82  53.33  92.64  85.06  10158 1313.438   578  273.320 1586.758
      69.21   0.00  59.75  41.61  92.64  85.06  10158 1313.438   578  273.320 1586.758
      69.21   0.00  75.12  31.79  92.64  85.06  10158 1313.438   578  273.320 1586.758
      69.21   0.00  94.13  31.79  92.64  85.06  10158 1313.438   578  273.320 1586.758
       0.00  86.02  15.60  32.07  92.64  85.06  10159 1313.761   578  273.320 1587.081
       0.00  86.02  94.86  32.07  92.64  85.06  10159 1313.761   578  273.320 1587.081

   ```
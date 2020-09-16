## gc
 * jdk8 默认gc收集器
   + java -XX:+PrintGCDetails -version
   + ps + parOld
 
### 根搜索算法
 * GC ROOTS
   + 虚拟机栈（栈桢中的本地变量表）中的引用对象
   + 方法区中类静态变量引用的对象
   + 方法区中常量引用的对象
   + 本地方法栈中JNI(一般说的native方法)的引用的对象
   
 * 引用
   + 强引用
   + 软引用：没有足够内存时回收
   + 弱引用：被弱引用关联的对象只能生存到下一次垃圾收集发生之前（每次gc时回收），回收只被弱引用关联的对象
   + 虚引用：希望一个对象被gc回收时，收到一个系统通知
   
 * 死亡时finalize 
   + 要经过两个标记过程
   + 一次标记，根据根搜索算法发现没有与GC ROOT引用的对象。
      - 判断没有覆盖过finalize和虚拟机执行过finalize, 虚拟机将这两种情况视为“没有必要执行”
      - 如果有则，把对象放到f-queue队列等待虚拟机执行， 如finalize有死循环，则回收系统崩溃。
   + 二次标记，如果在finalize中被拯救，则将它移除回收队列
   
### 方法区回收
 * 废弃常量
   + 如果回收"abc": 没有一个String对象 = "abc", 也没有其它地方引用这个字面量。如果有必要则回收常量池
   + 常量池中，接口、类、字段的符号引用也是如此
   
 * 无用的类
   + 该类的所有实例被回收
   + 加载该类的ClassLoader被回收
   + 该类对应的java.lang.Class对象没有在任何地方被引用，无法在任何地方通过反射访问该类的方法。
   + 仅仅可以被回收，不会是必然会回收
   + -Xnoclassgc, -verbose:class
   
### 垃圾回收算法
 * 标记，清除
   + 有内存碎片
   + 不利于后续大对象分配，如果没有足够空间，则还会执行gc
 * 复制
   + 浪费空间
   + 复制东西太多，不适合大于数对象长时间存活的场景
   + hotspot默认比是8:1, 新生代采用复制算法，尽量多回收，如果复制太多，效率将会变低
 * 标记，整理
   + 先标记，然后将存活对象都向一端移动，然后直接清理掉端边界外的内存
 * 分代
 
### 垃圾收集器
 * 垃圾回收算法是理论，垃圾收集器是实现。
 * Serial 串行收集器
   + 单线程收集器
   + 新生代与老年代 Serial Old
   + 新生代采用复制算法，老年代采用标记整理算法
   + 会暂停程序执行
   + DefNew：是使用-XX:+UseSerialGC（新生代，老年代都使用串行回收收集器）。
   + DefNewGeneration是default new generation 
 
 * ParNew 收集器
   + 默认开启收集线程数与CPU数量相同，当CPU过多时，可以通过-XX:ParallelGCThreads参数限制垃圾收集线程数
   + 会暂停程序执行
   + 新生代收集器
   + 在单CPU环境中绝对不会有比Serial收集器更好的效果
   + 除了Serial, 只有ParNew可以与CMS收集器配合工作
   + ParNew：是使用-XX:+UseParNewGC（新生代使用并行收集器，老年代使用串行回收收集器）
     - jdk8以后去掉了
     - 是Serial 多线程版本
     - 新生代复制，老年代标记整理
   + -XX:+UseConcMarkSweepGC(新生代使用并行收集器，老年代使用CMS)。
   + PS变为只用深度优先遍历。ParNew则是一直都只用广度优先顺序来遍历 
    
 * Parallel Scavenge
   + 新生代收集器
   + 采用复制算法
   + 目的是达到一个可控制的吞吐量
   + 吞吐量：CPU运行用户代码时间与CPU总消耗时间的比值
      - = 运行用户代码时间 / (运行用户代码时间 + 垃圾回收时间 )
   + 适合后台运算服务
   + -XX:MaxGCPauseMillis, 最大垃圾收集停顿时间
      - 通过牺牲吞吐量和新生代空间换取的
      - 之前是回收停顿时间是100ms, 10秒收集一次， 现在是停70ms, 5秒就要收集一次，收集次数上去总体时间上长，吞吐量下降
      - 同时收集时间小，垃圾回收不完，出现垃圾多。占用空间大
      
   + -XX:GCTimeRatio, 设置吞吐量大小
      - 0-100
      - 1 / (1 + 19) = 5%, 此参数为19， 默认为99, 垃圾回收占比是1%
   + -XX:+UseAdaptiveSizePolicy, 自适应调节策略
      - 只需要设置好-Xmx, 及上边两个参数之一后。
      - 不需要设置 -Xmn, Eden, Survivor占比， 晋升老年代对象年龄。它会自适应调节
      
   + PSYoungGen：是使用-XX:+UseParallelOldGC（新生代，老年代都使用并行回收收集器）
   + XX:+UseParallelGC（新生代使用并行回收收集器，老年代使用串行收集器）
      
  
 * Serial Old
   + 与Serial一样
   + 采用标记，整理算法
   + 可以与Parallel Scavenge使用，同是CMS收集出错时的一个后备预案
   
 * Parallel Old
   + 是Parallel Scavenge 的老年代版本， 多线程和标记整理算法
   + 与Parallel Scavenge 一起使用
 
 * CMS
   + 优点: 并发收集、低停顿
   + 采用标记，清除算法
   + 过程
     - 初始标记, 会stop the world, initial mark  
     - 并发标记 concurrent mark
     - 重新标记, 会stop the world, 比初始标记时间长，但比并发标记时间短很多。remark
     - 并发清除 concurrent sweep
   + 整个过程，最耗时的是并发标记，与并发清除
   + 缺点
     - 产生大量空间碎片、并发阶段会降低吞吐量
     - 1. CMS对CPU资源非常敏感， 默认回收线程数(cpu数 + 3) / 4 , 当大于4个cpu时最多占用不超过25%
     - 当小于4时，就很影响用户程序
     - 2. 无法处理浮动垃圾，因为并发清除，所以不能等老年代满了再执行回收，
     - -XX:CMSInitiatingOccupancyFraction来提高触发百分比
     - 如设置太高，导致没有足够的内存供用户线程执行，则会Concurrent Mode Failure
     - 会触发Serial Old gc 产生长时间停动
     - 3. 产生内存碎片，当分配大对象时由于没有足够连接空间而提前触发FullGc 
     - -XX:+UseCMSCompactAtFullCollection, 内存整理不能并发，所以会停顿时间变长
       - jdk8以后去掉了
     - -XX:CMSFullGCsBeforeCompaction, 多少个不压缩的FullGc后来一个压缩
       - jdk8以后去掉了
   + gc日志
   ``` 
    2020-03-07T19:42:40.306+0800: 234500.145: [GC (CMS Initial Mark) [1 CMS-initial-mark: 9005021K(11237376K)] 9139575K(12883392K), 0.0139099 secs] [Times: user=0.07 sys=0.01, real=0.01 secs]
    2020-03-07T19:42:40.320+0800: 234500.160: [CMS-concurrent-mark-start]
    2020-03-07T19:42:40.759+0800: 234500.598: [CMS-concurrent-mark: 0.438/0.439 secs] [Times: user=0.94 sys=0.00, real=0.43 secs]
    2020-03-07T19:42:40.759+0800: 234500.598: [CMS-concurrent-preclean-start]
    2020-03-07T19:42:40.772+0800: 234500.611: [CMS-concurrent-preclean: 0.013/0.013 secs] [Times: user=0.01 sys=0.00, real=0.02 secs]
    2020-03-07T19:42:40.772+0800: 234500.611: [CMS-concurrent-abortable-preclean-start]
     CMS: abort preclean due to time 2020-03-07T19:42:45.880+0800: 234505.719: [CMS-concurrent-abortable-preclean: 5.007/5.108 secs] [Times: user=5.13 sys=0.00, real=5.10 secs]
    2020-03-07T19:42:45.880+0800: 234505.720: [GC (CMS Final Remark) [YG occupancy: 226708 K (1646016 K)]2020-03-07T19:42:45.880+0800: 234505.720: [Rescan (parallel) , 0.0262083 secs]2020-03-07T19:42:45.906+0800: 234505.746: [we
    ak refs processing, 0.0018244 secs]2020-03-07T19:42:45.908+0800: 234505.748: [class unloading, 0.1477829 secs]2020-03-07T19:42:46.056+0800: 234505.896: [scrub symbol table, 0.0101040 secs]2020-03-07T19:42:46.066+0800: 234505
    .906: [scrub string table, 0.0014069 secs][1 CMS-remark: 9005021K(11237376K)] 9231729K(12883392K), 0.1877774 secs] [Times: user=0.37 sys=0.00, real=0.19 secs]
    2020-03-07T19:42:46.068+0800: 234505.908: [CMS-concurrent-sweep-start]
    2020-03-07T19:43:00.681+0800: 234520.521: [CMS-concurrent-sweep: 14.608/14.613 secs] [Times: user=15.08 sys=0.00, real=14.61 secs]
    2020-03-07T19:43:00.682+0800: 234520.522: [CMS-concurrent-reset-start]
    2020-03-07T19:43:00.700+0800: 234520.540: [CMS-concurrent-reset: 0.018/0.018 secs] [Times: user=0.02 sys=0.00, real=0.02 secs]
   ```
     
 * 参数
   + -XX:PretenureSizeThreshold 令大于这个设置值的对象直接在老年代中分配
     - 只对Serial, ParNew收集器有效
     
 * G1
   + 采用标记-整理算法
   + 在基本不牺牲吞吐量的前提下完成低停顿的内存回收 
   + garbage-first heap：是使用-XX:+UseG1GC（G1收集器）    

### 内存分配与回收
 * 大对象进入老年代
   + 1. eden中的大对象，但放不到survivor。通过担保进入老年代
   + 2. 直接创建的大对象，PretenureSizeThreshold 在Serial, ParNew收集器时有用
   + Parallel Scavenage 收集器有默认
   
 * 长期存活对象进入老年代
   + 在s1, s2 长期存活，达到最大年龄
   + -XX:MaxTenuringThreshold
   
 * 动态对象年龄判定
   + 无须达到MaxTenuringThreshold
   + 如果在survivor空间中相同年龄所有对象大小的总和大于survivor空间的一半，年龄大于或等于该年龄的对象就可以进入年老代
   
 * 空间分配担保
   + 如果每次晋升到老年代的平均大小大于老年代的剩余空间，才执行Full gc
   + 如果小于，查看HandlePromotionFailure设置是否允许担保失败；如果允许，则Minor gc
   + 否则，执行Full gc
   + 在jdk1.6 update24之后，HandlePromotionFailure参数不会影响虚拟机空间分配担保策略
   
### 分配速率, 提升速率, 大对象分配, Weak, Soft 及 Phantom 引用

### real time usr time sys time
 * real：指的是操作从开始到结束所经过的墙钟时间（WallClock Time）
 > 墙钟时间包括各种非运算的等待耗时，例如等待磁盘I/O、等待线程阻塞，
 > 而CPU时间不包括这些耗时，但当系统有多CPU或者多核的话，多线程操作会叠加这些CPU时间，所以看到user或sys时间超过real时间是完全正常的。
 * user：指的是用户态消耗的CPU时间；
 * sys：指的是内核态消耗的CPU时间。
 
 * real time时间大于usr time + sys time 问题
   + 一个是IO操作密集
   + 另一个是cpu(分配)的额度不够。
 
   
### 参数
 * -XX:+PrintGC（或者-verbose:gc）开启了简单GC日志模式
   + 无法从日志中判断是否 GC 将一些对象从 young generation 移到了 old generation
 ``` 
    [GC 246656K->243120K(376320K), 0.0929090 secs]
    [Full GC 243120K->241951K(629760K), 1.5589690 secs]
 ```
 * -XX:+PrintGCDetails
 ``` 
    [GC (Metadata GC Threshold) [PSYoungGen: 19427K->4592K(95232K)] 29052K->15061K(129536K), 0.0051148 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
    [Full GC (Metadata GC Threshold) [PSYoungGen: 4592K->0K(95232K)] [ParOldGen: 10469K->9121K(34304K)] 15061K->9121K(129536K), [Metaspace: 20783K->20783K(1067008K)], 0.0407688 secs] [Times: user=0.13 sys=0.00, real=0.04 secs] 
    [GC (Allocation Failure)) [PSYoungGen: 35510K->3651K(92160K)] 44632K->12780K(126464K), 0.0040306 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
    [Full GC (System.gc()) [PSYoungGen: 3651K->0K(92160K)] [ParOldGen: 9129K->9619K(34304K)] 12780K->9619K(126464K), [Metaspace: 22762K->22762K(1069056K)], 0.0613941 secs] [Times: user=0.27 sys=0.00, real=0.06 secs]
 ```
 * -XX:+PrintGCTimeStamps
   + 表示自 JVM 启动至今的时间戳会被添加到每一行中
 * -XX:+PrintGCDateStamps
 ``` 
    2014-01-03T12:08:38.102-0100: [GC 66048K->53077K(251392K), 0.0959470 secs]
    2014-01-03T12:08:38.239-0100: [GC 119125K->114661K(317440K), 0.1421720 secs]
    2014-01-03T12:08:38.513-0100: [GC 246757K->243133K(375296K), 0.2761000 secs]
 ```
 * -Xloggc
   + 缺省的 GC 日志时输出到终端的，使用 - Xloggc: 也可以输出到指定的文件
   + 这个参数隐式的设置了参数 - XX:+PrintGC 和 - XX:+PrintGCTimeStamps，但为了以防在新版本的 JVM 中有任何变化，我仍建议显示的设置这些参数。
   
 * -XX:+PrintHeapAtGC
   + GC前后堆的详细信息
 * -XX:+HeapDumpOnOutOfMemoryError， -XX:HeapDumpPath=E:\java\dump
   + 发生OutOfMemoryError时，转储堆快照, 
   + 类似通过jmap 输出的快照文件
   
 * -XX:+PrintTenuringDistribution
   + 问题
      - 这个打印的哪个区域的对象分布(survivor)
      - 是在gc之前打印，还是在gc之后打印(gc之后打印)
      - 一个新生对象第一次到survivor时其age算0还是算1
   > 对象的年龄就是他经历的MinorGC次数，对象首次分配时，年龄为0
   > MinorGC之后，若还没有被回收，则年龄+1，由于是第一次经历MinorGC
   > 因此进入survivor区。因此对象第一次进入survivor区域的时候年龄为1.
   
   
### 新创建对象
 * 新对象尝试栈上分配，不行再尝试TLAB分配，
 不行则考虑是否直接绕过eden区在年老代分配空间(-XX:PretenureSizeThreshold设置大对象直接进入年老代的阈值，
 当对象大小超过这个值时，将直接在年老代分配。)，不行则最后考虑在eden申请空间
 * 向eden申请空间创建新对象，eden没有合适的空间，因此触发minor gc
 * minor gc将eden区及from survivor区域的存活对象进行处理
   + 如果这些对象年龄达到阈值，则直接晋升到年老代
   + 若要拷贝的对象太大，那么不会拷贝到to survivor，而是直接进入年老代
   + 若to survivor区域空间不够/或者复制过程中出现不够，则发生survivor溢出，直接进入年老代
   + 其他的，若to survivor区域空间够，则存活对象拷贝到to survivor区域
 * 此时eden区及from survivor区域的剩余对象为垃圾对象，直接抹掉回收，释放的空间成为新的可分配的空间
 * minor gc之后，若eden空间足够，则新对象在eden分配空间；若eden空间仍然不够，则新对象直接在年老代分配空间
 
 * 栈上分配 
   + 通过编译器逃逸分析，如果不会逃逸出栈之外被访问到，则直接在栈上分配。避免垃圾收集器处理
   + 逃逸分析
     - 是JIT发现热点代码后，才会进行逃逸分析
     - -XX:+DoEscapeAnalysis
     - 从jdk 1.7开始已经默认开始逃逸分析
     - （开启逃逸分析需要设置jvm参数）
     - 分析对象动态作用域，当一个对象在方法中定义后，可能被外部方法所引用
     - 方法逃逸：如：将引用作为参数传递到其它方法
     - 线程逃逸：如：有可能被其它线程访问到，如赋值给类变量或其它线程中访问的实例变量
     - 锁消除
     > -XX:+EliminateLocks开启锁消除
     > jdk1.8默认开启
     - 标量替换
     > -XX:+EliminateAllocations开启标量替换(jdk1.8默认开启)
     > 标量（Scalar）是指一个无法再分解成更小的数据的数据。Java中的原始数据类型就是标量
     > 那些还可以分解的数据叫做聚合量（Aggregate），Java中的对象就是聚合量，因为他可以分解成其他聚合量和标量。
     > 在JIT阶段，如果经过逃逸分析，发现一个对象不会被外界访问的话，那么经过JIT优化，就会把这个对象拆解成若干个其中包含的若干个成员变量来代替。这个过程就是标量替换。
     
 * 对象内存分配的两种方法
   + 为对象分配空间的任务等同于把一块确定大小的内存从Java堆中划分出来。
   + 指针碰撞(Serial、ParNew等带Compact过程的收集器)
   >假设Java堆中内存是绝对规整的，所有用过的内存都放在一边，空闲的内存放在另一边，中间放着一个指针作为分界点的指示器，
   >那所分配内存就仅仅是把那个指针向空闲空间那边挪动一段与对象大小相等的距离，这种分配方式称为“指针碰撞”（Bump the Pointer）。
   + 空闲列表(CMS这种基于Mark-Sweep算法的收集器)
   >如果Java堆中的内存并不是规整的，已使用的内存和空闲的内存相互交错，那就没有办法简单地进行指针碰撞了
   >虚拟机就必须维护一个列表，记录上哪些内存块是可用的
   >在分配的时候从列表中找到一块足够大的空间划分给对象实例，并更新列表上的记录，这种分配方式称为“空闲列表”（Free List）。
     
 * TLAB分配
   + 默认情况下TLAB是打开状态
   + 本地线程分配缓冲（Thread Local Allocation Buffer, TLAB）
   + 由于
     - 对象创建在虚拟机中是非常频繁的行为
     - 即使是仅仅修改一个指针所指向的位置，在并发情况下也并不是线程安全的
     - 可能出现正在给对象A分配内存，指针还没来得及修改，对象B又同时使用了原来的指针来分配内存的情况。
   + 解决这个问题有两种方案
     - 一种是对分配内存空间的动作进行同步处理——实际上虚拟机采用CAS配上失败重试的方式保证更新操作的原子性；
     - 另一种是把内存分配的动作按照线程划分在不同的空间之中进行，即每个线程在Java堆中预先分配一小块内存，称为本地线程分配缓冲（Thread Local Allocation Buffer, TLAB）。
     
   + 哪个线程要分配内存，就在哪个线程的TLAB上分配，只有TLAB用完并分配新的TLAB时，才需要同步锁定
   + 虚拟机是否使用TLAB，可以通过-XX:+/-UseTLAB参数来设定。
   + 通常默认的TLAB区域大小是Eden区域的1%，当然也可以手工进行调整，对应的JVM参数是-XX:TLABWasteTargetPercent。
   + 内存分配完成后，虚拟机需要将分配到的内存空间都初始化为零值（不包括对象头）
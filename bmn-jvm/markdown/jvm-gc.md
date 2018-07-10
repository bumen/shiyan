## gc
 
### 根搜索算法
 * GC ROOTS
   + 虚拟机栈（栈桢中的本地变量表）中的引用对象
   + 方法区中类静态变量引用的对象
   + 方法区中常量引用的对象
   + 本地方法栈中JNI(一般说的native方法)的引用的对象
   
 * 引用
   + 强引用
   + 软引用：没有足够内存时回收
   + 弱引用：每次gc时回收
   + 虚引用：希望一个对象被gc回收时，收到一个系统通知
   
 * 死亡时finalize 
   + 要经过两个标记过程
   + 一次标记，根据根搜索算法发现没有与GC ROOT引用的对象。
      - 判断没有覆盖过finalize和虚拟机执行过finalize, 虚拟机将这两种情况视为“没有必要执行”
      - 如果有则，把对象放到f-queue队列等待虚拟机执行， 如finalize有死循环，则回收系统崩溃。
   + 二次标记，如果在finalize中被拯救，则将它移除回收队列
   
### 方法回收
 * 废弃常量
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
 * 标记，整理
 * 分代
 
### 垃圾收集器
 * 垃圾回收算法是理论，垃圾收集器是实现。
 * Serial 串行收集器
   + 单线程收集器
   + 采用复制算法
   + 会暂停程序执行
   + 新生代与老年代 Serial Old
 
 * ParNew 收集器
   + 是Serial 多线程版本
   + 默认开启收集线程数与CPU数量相同，当CPU过多时，可以通过-XX:ParallelGCThreads参数限制垃圾收集线程数
   + 会暂停程序执行
   + 新生代收集器
   + 在单CPU环境中绝对不会有比Serial收集器更好的效果
   + 除了Serial, 只有ParNew可以与CMS收集器配合工作
    
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
      
  
 * Serial Old
   + 与Serial一样
   + 采用标记，整理算法
   + 可以与Parallel Scavenge使用，同是CMS收集出错时的一个后备预案
   
 * Parallel Old
   + 是Parallel Scavenge 的老年代版本， 多线程和标记整理算法
   + 与Parallel Scavenge 一起使用
 
 * CMS
   + 获取最短回收停顿时间
   + 采用标记，清除算法
   + 过程
     - 初始标记, 会stop the world, initial mark  
     - 并发标记 concurrent mark
     - 重新标记, 会stop the world, 比初始标记时间长，但比并发标记时间短很多。remark
     - 并发清除 concurrent sweep
   + 整个过程，最耗时的是并发标记，与并发清除
   + 缺点
     - 1. CMS对CPU资源非常敏感， 默认回收线程数(cpu数 + 3) / 4 , 当大于4个cpu时最多占用不超过25%
     - 当小于4时，就很影响用户程序
     - 2. 无法处理浮动垃圾，因为并发清除，所以不能等老年代满了再执行回收，
     - -XX:CMSInitiatingOccupancyFraction来提高触发百分比
     - 如设置太高，导致没有足够的内存供用户线程执行，则会Concurrent Mode Faliure
     - 会触发Serial Old gc 产生长时间停动
     - 3. 产生内存碎片，当分配大对象时由于没有足够连接空间而提前触发FullGc 
     - -XX:+UseCMSCompactAtFullCollection, 内存整理不能并发，所以会停顿时间变长
     - -XX:CMSFullGCsBeforeCompaction, 多少个不压缩的FullGc后来一个压缩
     
 * 参数
   + -XX:PretenureSizeThreshold 令大于这个设置值的对象直接在老年代中分配
     - 只对Serial, ParNew收集器有效
     
 * G1
   + 采用标记-整理算法
   + 在基本不牺牲吞吐量的前提下完成低停顿的内存回收     

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
   + 如果每次晋升到老年代的平均大小老年代的剩余空间，才执行Full gc
   + 如果小于，查看HandlePromotionFailure设置是否允许担保失败；如果允许，则Minor gc
   + 否则，执行Full gc
   + 在jdk1.6 update24之后，HandlePromotionFailure参数不会影响虚拟机空间分配担保策略
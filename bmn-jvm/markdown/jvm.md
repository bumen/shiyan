## jvm-java

### java技术体系（包括哪几块）
 * java程序设计语言
 * jvm
 * class字节码文件格式
 * java API类库
 * 第三方java类库
 * JDK组成
   + java程序设计语言
   + jvm
   + java API类库
 * JRE组成
   + java API类库中java se api子集
   + jvm

### jvm 运行时数据区
 * 堆，栈，程序计数器，本地方法栈，方法区（java8已移除），元空间（java8新增）
 * 程序计数器
   + 记录下一条需要执行的字节码指令
   + 线程私有
 * 栈
   + 线程私有
   + 由栈帧组成
   
 * 本地方法栈
   + 与栈一样
   
 * Java堆
   + 
 * 方法区
   + 用于存放被jvm加载的类信息，常量，静态变量，即时编译编译后的代码等数据
   + 还有常量池
 * 运行时常量池
   + 是方法区的一部分 
   + 是类中常量池，在类加载后存放到方法区的运行时常量池中。
 * 直接内存
   + 设置jvm参数-Xmx时需要注意预留直接内存
   
### jvm异常
 * 栈
   + StackOverFlowError: 当达到栈最大深度时抛出。可能是达到设置的最大栈深度。可能是未达到最大栈深度但，内存不够不能创建栈桢；
   + OutOfMemoryError: 当创建线程时，由于内存不够，无法为线程分配栈空间抛出。
   
 * 方法区溢出
   + 大量动态生成class
   + 大量jsp或动态产生jsp文件应用 
   + 基于OSGI应用（即使同一个类，不同类加载器加载也会视为不同类）
   
 * 直接内存溢出
   + 通过-XX:MaxDirectMemorySize指定，如果不指定默认与Java堆的最大值-Xmx一样。
   + OutOfMemoryError
 
### 垃圾收集器 
 * 看jvm-gc.md

#### 对象死亡过程
 * https://www.iteye.com/blog/bijian1013-2288223
 
 * 通过根搜索，要真正宣告一个对象死亡，**至少**要经历两次标记过程
   + 第一次标记：对没有引用的对象标记并且进行一次筛选。
   + 筛选：看finalize
   + 第二次标记，如果对象没有被重新引用，则它就真的离死亡不远了。(可以在finalize方法中再重生)
   + 后续可能不同收集器可能还用有不同的处理过程
   
#### finalize
 * finalize方法通常是用于清理一些非本地方法(native)，和一些对象安全释放校验的操作
 * Finalizer线程：是daemon线程，由虚拟机创建并启动处理要执行的finalize方法的对象
   + 通过调用ReferenceQueue的remove方法获取要执行finalize方法的对象
   + 当执行finalize方法时，虚拟机不承诺会等待它运行结束。
   + 如果一个对象的finalize方法中执行缓慢，或发生死循环。将导致F-Queue中其他对象永久处行等待状态，甚至导致整个内存回收系统崩溃。
   + 是不同线程执行，避免引起线程安全问题
   
 * 在收集垃圾过程中筛选出要执行的finalize方法对象
 * 筛选：此对象是否有必要执行finalize.
 * 如果对象没有实现finalize方法，或finalize方法已经被虚拟机调用过，则是没有必要执行
 * 如果对象判断有必要执行finalize方法，那么把对象放到ReferenceQueue.
 * 此时由Finalizer线程执行
 * 特点
   + 没有重写finalize方法，则直接回收
   + 重写finalize方法，垃圾回收时，一个对象的finalize方法一生只会被执行一次，即使重生过。因为Finalizer线程执行完会标记对象为finalized状态
   + 手动调用finalize方法，不会影响对象标记状态。
 
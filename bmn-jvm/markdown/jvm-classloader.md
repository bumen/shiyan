## 类加载


### 类加载时机
  * 类生命周期
    + 加载
    + 链接
      - 验证
      - 准备
      - 解析
    + 初始化
    + 使用
    + 卸载 
  * 加载、验证、准备、初始化、卸载必须顺序执行
  * 主动初始化
    + new、getstatic、putstatic、invokestatic会角触发初始化
    + 通过反射调用类
    + 初始化一个类，先初始化父类
    + main方法所在的类
    + 只有这4种情况会触发主动初始化
  * 被动初始化
    + 通过子类访问父类静态属性或方法。只会初始化父类
    + 创建数组
    + static final 属性，编译器会把变量值，直接编译到引用类的常量池中
    
### 加载过程
  * 加载
    + 读取字节码文件
    + 将字节码流所代表的静态存储结构转化为方法区的运行时数据结构
    + 在java堆中生成一个代表这个类的Class对象，作为方法区数据的访问入口
    
  * 验证
    + 文件格式验证
      - 验证通过后字节流才会进入内存方法区中进行存储
      - 其它验证全部是基于方法区的存储结构进行
      - 与加载阶段交叉进行
      
  * 解析
    + 是虚拟机将常量池内的符号引用替换为直接引用的过程
      - CONSTANT_Class_info
      - CONSTANT_Fieldref_info
      - CONSTANT_Methodref_info
      - CONSTANT_InterfaceMethodref_info
  * 初始化
    + <cinit>保证多线程正确地加锁同步。只有一个线程会执行。
   
### 加载注意
  * SPI加载
  * OSGI加载
  * ClassLoader类的链接
  * 注意同一个类加载器的实例和同一个class文件只能被加载器一次，多次加载将报错
    + 实现热部署需要必须让同一个class文件可以根据不同的类加载器重复加载
  
### 双亲委派模型的破坏者: Thread.currentThread.getContextClassLoader()
  >在Java应用中存在着很多服务提供者接口（Service Provider Interface，SPI），
  这些接口允许第三方为它们提供实现，如常见的 SPI 有 JDBC、JNDI等，这些 SPI 的接口属于 Java 核心库，
  一般存在rt.jar包中，由Bootstrap类加载器加载，而 SPI 的第三方实现代码则是作为Java应用所依赖的 jar 包被存放在classpath路径下，
  由于SPI接口中的代码经常需要加载具体的第三方实现类并调用其相关方法，但SPI的核心接口类是由引导类加载器来加载的，
  而Bootstrap类加载器无法直接加载SPI的实现类，同时由于双亲委派模式的存在，
  Bootstrap类加载器也无法反向委托AppClassLoader加载器SPI的实现类。
  在这种情况下，我们就需要一种特殊的类加载器来加载第三方的类库，而线程上下文类加载器就是很好的选择。
  
  * 线程上下文加载器
  * 通过setContextClassLoader指定一个类加载器
  * 默认context加载器是appclassloader由Launch设置
  * rt.jar也是由appclassloader加载
  * 问题
    + 对时spi接口，一般由appclassloader加载
    + 而spi的实现，可能由不同的自定义类加载器加载
    + 因此接口 != 接口实现。不是相同的类加载器
  * 为了解决上述问题
    + 通过contextclassloader实现用指定的类型加载器接口实现 
  
### jvm判断两个类是否相等
 * 类的完整类名必须一致，包括包名。
 * 加载这个类的ClassLoader(指ClassLoader实例对象)必须相同。
 
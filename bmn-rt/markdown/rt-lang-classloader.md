## classloader
 * Bootstrap c实现 jvm启动创建
   + sun.boot.class.path
 * ExtClassLoader jvm启动Launcher创建
   + java.ext.dirs
 * AppClassLoader(即SystemClassLoader) jvm启动时通过Launcher创建
   + java.class.path

### loadClass方法是加载类的入口
 * 过程是采用双亲委派方式加载类
   1. 如果从当前类加载器中找到已经加载过的类，返回。否则执行2
   2. 调用父类的loadClass方法。 如果父类加载器返回null, 表示没找到。执行3
   3. 从父类加载器，加载类。加载成功，返回。失败，执行4
   4. 从父类加载器中没有找到，则从当前类加载
   
 * 类加载时
  1. findClass , 查找类文件 
  2. definedClass , 查找完成，返回 生成Class<?> 对象
  3. resolveClass, 链接生成的Class<?>对象, 可以控制是否链接
  
 * 自定义类加载器时
   + 只需要重写findClass就可以了，
   + 同需要手动调用definedClass 生成Class<?> 对象
   
   
### getSystemClassLoader
 * 总返回 AppClassLoader， 
 * 在jvm启动时，通过Launcher类创建的，而Launcher是一个单历、
   + Launcher 的loader即AppClassLoader对象
   + 而getSystemClassLoader， 返回的是Launcher.loader对象
   
   
### 加载过程
 1. 加载
   * 通过自定义classLoader， 重写findClass， 加载不同来源的类
 2. 链接
   * 校验, 类是否正确 
   * 准备, 为静态变量分配空间，并初始化
   * 解析, 把符号引用转为直接引用 
 3. 初始化
   * 主动引用  
    1. new  
    2. 执行类的静态属性或方法   
    3. 对类进行反射调用   
    4. 初始一个类，先初始化父类  
    5. jvm启动时，先初始化包含main方法的类  
    6. Class.fromName()  
    
   * 被动引用, 不初始化  
    1. 子类引用父类静态字段, 不会导致子类初始化  
    2. 创建数组类，不会初始化  
    3. A类引用B类的static final 属性时，不会导致B类初始化
    
### static final 静态常量
 * 为什么没有加载定义了静态常量的类
    >因为编译器优化了
  把A类的定义的常量NAME, 直接放到了使用者B类的常量池中，以后B中对常量A.NAME引用实际都被转化为B类对自身常量池的引用。
  B类class文件中并没有A类的符号引用，两个类在编译成class后就不存在任何联系了
  所以在使用A.NAME时，不会加载A类
    
    
### loadLibrary过程
 1. 判断查找是否是绝对路径
   + 是绝对路径，则直接查找这个路径文件，返回
   + 不是绝对路径，走 2.
 2. 如果ClassLoader子类实现findLibrary方法
   + 则在自定路径中查找，返回
   + 没有实现findLibrary方法，走 3.
 3. 查找system path 
   + sun.boot.library.path
   + 找到返回
 4. 查找user path
   + java.library.path
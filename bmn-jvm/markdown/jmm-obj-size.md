## java对象大小

### java对象模型-OOP-Klass二分模型
 + OOP: Ordinary Object Point (普通对象指针), 指向java对象实例
 + Klass: 用来描述Java类，包含了元数据和方法信息等
 
### instanceKlass
 + 当类加载器加载类时，会创建一个instanceKlass对象，表示这个类的运行时元数据
 + instanceKlass存放在方法区
 
### instanceOOP
 + 当创建一个类的对象时，会创建一个instanceOOP
 + instanceOOP存放在堆
 
### java对象内容
 * 普通对象
   + 对象头
   + 元数据指针，类对象指针，指向方法区的instanceKlass对象
   + 实例数据
   
 * 数组对象
   + 对象头
   + 元数据指针
   + 数组长度
   + 实例数据
   
 * 对象头
   + 对象头长度与JVM字长一致，32位jvm的对象头是32位，64位jvm的对象头是64位
 * 元数据指针
   + 如果是32G内存以下的，默认开启对象指针压缩，4个字节
 * 数组长度
   + 占4个字节
 * padding
   + 内存对齐，按照8的位数对齐
### java对象大小
 * 计算过程
   1. 除了对象整体需要按8字节对齐外，每个成员变量都尽量使本身的大小在内存中尽量对齐。比如 int 按 4 位对齐，long 按 8 位对齐。
   2. 类属性按照如下优先级进行排列：长整型和双精度类型；整型和浮点型；字符和短整型；字节类型和布尔类型，最后是引用类型。
   这些属性都按照各自的单位对齐。
   3. 优先按照规则一和二处理父类中的成员，接着才是子类的成员。
   4. 当父类中最后一个成员和子类第一个成员的间隔如果不够4个字节的话，就必须扩展到4个字节的基本单位。
   5. 如果子类第一个成员是一个双精度或者长整型，并且父类并没有用完8个字节，
   JVM会破坏规则2，按照整形（int），短整型（short），字节型（byte），引用类型（reference）的顺序，向未填满的空间填充。
   
 * 对象头(8) + oop(4/8) + 数据区 + padding
 * 未压缩普通Object对象大小
   + 8 + 8 = 16字节
 * 压缩普通Object对象大小
   + 8 + 4 = 12 + 4(padding)=16字节
 * 未压缩数组对象大小
   + 8 + 8 + 4 + 4(padding) = 24字节
   + 如：new Object[3]在放三个对象后大小 = 24 + 16 * 3
 * 压缩数组对象大小  
   + 8 + 4 + 4 =  16字节
   + 如：new Object[3]在放三个对象后大小 = 16 + 16 * 3  
 
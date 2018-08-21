## jvm specification 7

### 浮点数
 * 严格按IEEE 754标准实现
 * jvm与IEEE 754浮点运算的不同
   + 在浮点运算时，不会抛出异常
   + 不支持信号浮点比较
   + jvm舍入模式永远使用最接近舍入模式（4舍5入），但是浮点数转化为整形数是使用向0舍入。
 * 包括
   + 正负带符号可数数值
   + 正负0
   + 正负无穷大
   + NaN(表示某些无效的运算操作，如除数为0)
 * 浮点模式
   + 通过方法ACC_STRICT标志位实现
 * 数值集合转换
 * NAN
   + 是无序的，对它进行任何的数值比较和等值测试都会返回false。
   + 任何数字与NaN进行非等值比较都会返回true.
   + 如果有其中一个操作为NaN, 则所有浮点型的比较指令都失败
### 字节码指令集
 * 加载和存储指令
 * 运算指令
   + iinc： 直接对局部变量进行自增操作
   + lcmp 
    `long类型比较，jvm采用带符号比较方式`
   + dcmpg, dcoml, fcomg, fcoml
     `浮点数比较，jvm采用IEEE 754规范定义的无信号比较`
      - v1 > v2  把1压入操作数栈
      - v1 == v2 把0压入操作数栈
      - v1 < v2  把-1压入操作数栈
      - 然后都ifle, ifge 把操作数栈值与0比较
      - g, l 用来处理一个操作是NaN的情况
      - 当一个操作是NaN，dcmpg, fcomg 把1压入操作数栈
      - 当一个操作是NaN, dcmpl, fcoml 把-1压入操作数栈
      
   + ~x = -1 ^ x
   + a^a = 0, (a^b)^a = (a^a)^b = 0^b = b
   
 * 类型转换指令
   + 宽化类型转换
      - int  到 long, float, double
      - long 到 float, double
      - float到 double
   + 窄化类型转换
      - i2b, i2c, i2s, l2i, f2i, f2l, d2i, d2l, d2f
      - 窄化类型转换可能会导致结果产生不同的正负号，不同的数量级，转换过程可能会丢失精度
      - 在将int, long类型窄化为整数类型T的时候，简单的丢弃除最低位N个字节以外的内容。可能会导致转换结果与输入值有不同正负号
      - 将浮点值窄化为整数类型T（T限于int或long类型之一）
      >> 1. 如果浮点值NaN，转为0
      >> 2. 如果浮点值不是无穷大，浮点值使用IEEE 754的向0舍入模式取整
          - 如果T是long, 并且结果v在long类型取值内，那使用结果v
          - 如果T是int, 并且结果v在int类型取值内，那使用结果v
          - 如果结果V的值太小，无法使用int,long表示，则使用int, long最小值
          - 如果结果V的值太大，无法使用int,long表示，则使用int, long最大值 
      - 将double转float
      >> 使用最近舍入模式，如果转换结果的绝对值太小无法使用float来表示，将返回float正负0; v绝对值太大， 使用float正负无穷大；NaN转为float NaN
      
 * 对象创建与操作指令
   + new
     - 创建类实例对象
   + newarray, anewarray, mutilanewarray
     - 创建数组
   + 访问字段
     - getfiled, putfield
     - getstatic, putstatic
   + 将一个数组元素加载到操作数栈
     - baload, saload, caload, iaload, laload, daload, faload, aaload
   + 将一个操作数栈的值储存到数组元素中
     - bastore, satore, castore, iastore, lastore, dastore, fastore, aastore
   + 数组长度
     - arraylength
   + 检查类实例类型
     - instanceof, checkcast
     
 * 操作数栈管理指令
   + pop, pop2, dup, dup2, dup_x1, dup_x2, dup2_x1, dup2_x2, swap
 
 * 控制转移指令
   + 条件分支：  
   `ifeq, iflt, ifle, ifne, ifgt, ifge, ifnull, ifnonnull, if_icompeq, if_icompne, if_icomplt, if_icompgt, if_icomple, if_icompge, if_acompeq, if_acompne`
      - 有专门指令用来处理int和reference类型的条件分支比较操作
   + 复合条件分支： tableswitch, lookupswitch
   + 无条件分支：goto, goto_w, jsr, jsr_w, ret
   >> byte, char, short, boolean条件分支比较操作，使用int类型比较指令完成
   >> long, float, double 条件分支比较操作，则会先执行相应类型的比较运算指令，运算指令会返回一个整形值到操作数栈中，随后再执行Int类型的条件分支比较操作完成。
   >> 所有int类型的条件分支转移指令进行的都是有符号比较操作
   
 * 方法调用和返回指令
   + invokevirtual: 调用对象实例方法
   + invokeinterface: 调用接口方法，找到一个实现了接口的方法的对象，找出适合方法调用
   + invokespecial: 调用一些特殊的实例方法，包括实例初始化方法，私有方法，父类方法
   + invokestatick: 调用类方法
   + ireturn: boolean, byte, char, short, int 返回值类型
   + lreturn, dreturn, freturn, areturn
   + return: 为void方法，实例初始化方法，类和接口的初始化方法使用
 
 * 抛出异常指令
   + athrow：显示抛出异常操作
   + 还有别的异常会在其他javajvm指令检测到异常状态时由jvm自动抛出。
   
 * 同步指令
   + 方法同步，代码块同步，都通过monitor来支持。
   + 方法同步使用ACC_SYNCHROINZED 标识来实现
   + 代码块同步使用monitorenter, monitorexit
  
 * ldc, ldc_w, ldc2_w
   + 访问类型为int, long, float, double的数据，以及表示String实例的引用类型数据
   + ldc, ldc_w用来访问运行时常量池中的对象，包括String实例，但不包括long, double
   + 当使用的运行时常量池的项数目多于256，一个字节表示的范围时，需要使用ldc_w指令取代ldc
   + ldc2_w 用于访问类型为double,long的运行时常量池项
   
### 编译器
 * 异常
   + 如果catch块中又抛出了异常，如果方法中没有捕获，方法异常结束，则通过athrow抛出给调用者
   + 如果finally块中抛出了异常，如果方法中没有捕获，方法异常结束，则由jvm自动抛出给调用者
   
   
### 字节码属性
 * jvm必须实现: ConstantValue, Code, Exceptions, InnerClasses, EnclosingMethod, Synthetic
 * jvm必须实现: RuntimeVisibleAnnotations, RuntimeInvisibleAnnotations, RuntimeVisiableParameterAnnotations, RuntimeInvisiableParameterAnnotations, AnnotationDefault
 * jvm必须实现: Signature, StackMapTable, BootstrapMethods
 
 * ConstantValue: 位于field_info结构的属性表中。
   +只有static字段时，使用
   +值：long, float, double, integer(i, s, c, b, z), string
   
 * Code：位于method_info结构的属性表
   + 如果方法被声明为native或abstract类型，那么对应method_info结构不能有明确的Code属性。
   + 其它情况下必须有Code属性
   + max_stack: 给出了当前方法在操作数栈在运行执行的任何时间点的最大深度
   + max_locals: 指分配在当前方法引用的局部变量表中的局部变量个数
     - long, double占2个索引，其它占一个索引
   + code[]数组
     - 有的字节码指令有直接操作数，可能这些指令的实际长度超过一个字节
     - 因此bytecode指令列表索引显示可能是不连续的
   + exception table
     - startpc: code[]数组中的指令索引
     - endpc：code[]数组中的指令索引, 不包括endpc索引值
     - 当程序计数器在范围[startpc, endpc)时，即当执行x索引的指令时发生了异常，且startpc <= x < endpc时，处理异常处理器
     - handlerpc: code[]数组中指令索引
     - 当与异常处理器匹配时(是指定类的或其子类)，执行handler_pc指令向的指令
   + attributes[]: code属性下的属性
     - 只能是LineNumberTable, LocalVariableTable, LocalVariableTypeTable, StackMapTable
     - 必须忽略其它出现的所有不能识别的属性
     
 * StackMapTable: 位于Code属性的属性性中，在虚拟机类加载的类型阶段被使用
   
 * Exceptions: 位于method_info结构的属性表中
   + 指出一个方法需要检查的可能抛出的异常
   + 通过exception_index_table[]数组指定
   + 一个方法如果要抛出异常
      - 抛出的是RuntimeException或其子类实例
      - 抛出Error或其子类的实例
      - 抛出的是在exception_index_table[]数组中申明的异常或其子类实例
      
 * InnerClasses: 位于ClassFile结构属性表（最外层Attributes中）
   + 一个类中的内部类信息
 * EnclosingMethod: 位于ClassFile结构属性表
   + 当class为局部类或者匿名类时才能具有此属性
   
 * Synthetic: 位于ClassFile结构属性表
   + 如果一个类成员没有在源文件中出现，则要使用此属性
 * Signature: 位于ClassFile, field_info, method_info
   + 记录泛型签名信息
   
 * LineNumberTable: 位于Code结构的属性表
   + 它被调试器用于确定源文件行号表示的内容在code[]数组中对应的部分。
   
 * LocalVariableTable: 位于Code结构的属性表
   + 它被调试器于确定方法在执行过程中局部变量的信息
   + local_variable_table[]
     - startpc, length: 确定变量作用域[startpc, startpc + length)
     - index: 变量索引
     - long, double 占两个索引，其它占一个索引
     
 * LocalVariableTypeTable: 位于Code结构的属性表
   + 它被用于给调试器确定方法在执行中局部变量的信息
   + 提供签名信息而不是描述信息
   + 仅对泛型类型有意义，泛型类型的属性同时出现在LocalVariableTable与此属性中
   + [startpc, startpc + length)
   
 * Deprecated: 位于ClassFile, field_info, method_info
   + 记录标记为@Deprecated的类，接口，方法，字段
 
 * RuntimeVisibleAnnotations: 位于ClassFile, field_info, method_info结构属性中
   + 保存类，字段，方法上运行时可见的注解
   
 * RuntimeVisibleParameterAnnotations: 位于method_info结构属性中
   + 保存方法参与的所有运行时可见注解
   
 * AnnotationDefault: 位于method_info结构属性中
 
 * BootstrapMethods: 位于ClassFile结构的属性表中
   + 用于保存invokedynamic指令引用的引导方法限定符
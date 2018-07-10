## Class 字节码

### Class 结构
 * u4 魔数
 * u2 minor version
 * u2 major version 
   + java 1.0 45
   + java 1.1 46
   + java 1.7 51
   + java 1.8 52
 * u2 constant_pool_count
 * cp_info
 * u2 access_flags
 * u2 this_class
 * u2 super_class
 * u2 interface_count
 * u2 interfaces
 * u2 field_count
 * field_info
 * u2 method_count
 * method_info
 * u2 attribute_count
 * attribute_info
 > this_class, super_class, interfaces 确定类的继承关系
 
### 常量池
 * constant_pool_count
   + 从1开始的长度
   + 0表达“不引用任何一个常量池项目”
 * 常量池存放两大类
   + 字面量
     - integer
     - float
     - long
     - double
     - string
   + 符号引用
     - class
     - fieldref
     - methodref
     - interfacemethodref
   
 * 常量池中数据11种类型
   + 每一个类型都是一个表结构
     - 1-12 表示11种类型
     - 没有2类型
     - 1 utf8, utf8编码字符串
     - 3 integer, 整形字面量
     - 4 float, 浮点字面量
     - 5 long, 长整形字面量
     - 6 double, 双精度浮点字面量
     - 7 class, 类或接口符号引用
     - 8 string, 字符串字面量
     - 9 fieldref, 字段符号引用
     - 10 methodref, 类中方法符号引用
     - 11 interfacemethodref, 接口中方法符号引用
     - 12 nameandtype, 字段或方法的部分符号引用
   + 每一个类型表开始第一位是一个u1 的标志位tag，表示个具类型
     - u1 3, 表示CONSTANT_Integer_info
     
   + CONSTANT_Utf8_info
     - u1 tag, 1
     - u2 length 65536
     - u1 bytes( u2 bytes, u3 bytes), 所有字符在\u0001-\u007f用一个字节，在\u0080-\u07ff用两个字节，在\u0800-\uffff用三个字节表示
     
   + CONSTANT_NameAndType_info
     - u1 tag, 12
     - u2 index, 指向该字段或方法名称常量项索引
     - u2 index, 指向该字段或方法描述符常量项索引
     
### 概念
 * 全限定名
   + 类全限定名，org/app/Test.class;, 把.换成/， ;结尾
 * 字段或方法简单名称
   + public void say(), say
   + private int name, name
 * 描述符
   + 描述字段数据类型
   + 描述方法参数列表和返回值
   + 规则
     - B, byte
     - C, char
     - D, double
     - F, float
     - I, int
     - J, long
     - S, short
     - Z, boolean
     - V, void
     - L, Ljava/lang/Object; L加对象全限定名
     - [, 数组[I, [Ljava/lang/Object;
     
### 字段表集合
 * 字段信息
   + 作用域, private public protected
   + 类变量或实例变量, static
   + 可变性, final
   + 并发可见性, volatile
   + 可否序列化, transient
   + 字段类型, 基础数据类型, 对象, 数组
   + 字段名称
 * 表结构
   + u2 access_flags
   + u2 name_index, 简单名称
   + u2 descriptor_index
   + u2 attribute_count
   + attribute_info
 
### 方法表集合
 * 表结构与字段表集合一样
 * 有重写方法，则集合中会出现父类被重写方法
 * 重载
   + 简单名称相同
   + 特征签名不同：只包括参数，不包括返回值
   
### 属性表集合
 * Class文件，字段表，方法表中都可以携带自己的属性表集合
 * 通过编码器实现
 * 每个属性名称需要从常量池中引用一个CONSTANT_Utf8_info类型的常量来表示
 * 结构
   + u2 attribute_name_index
   + u4 attribute_length
   + u1 info
 
#### Code属性
 * 方法体里的代码经过javac编译处理之后，最终变为字节码指令存储在Code属性内
 * 结构
   + u2 attribute_name_index
   + u4 attribute_length
   + u2 max_stack
   + u2 max_locals
   + u4 code_length
   + u1 code
   + u2 exception_table_length
   + exception_info
   + u2 attrubute_count
   + attribute_info
 
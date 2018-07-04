## chapter 8 java字节码

### 字节码格式. 描述符
 * 类名， 类与接口
   + 全包名+类名
   + 把"."换成"/"
   + java.lang.String-> java/lang/String
 * 基本类型描述符， 域
   + byte char short int long float double boolean
   + B,   C,   S,    I,  J,   F,    D,     Z
 * 对象类型描述符， 域
   + 全名前加"L"前缀，加";"后缀
   + Ljava/lang/String;
   
 * 数组类型描述符， 域
   + 在元素类型前加"["前缀
   + 一个"[", 表示一个维度
   + 二维double数组-> [[D
   
 * 方法描述符， 方法
   + 取决于，参数与返回值的类型
   + (参数类型)返回值类型
   + 返回值为void 是 V表示
   + int calc(String str) -> (Ljava/lang/String;)I
   
 * 类，域，方法的类型签名， 随着泛型加入的
   + 目的：通过反射获取泛型相关信息
   
### 常量池
 * CONSTANT_UTF8_info 表示常量值
 * CONSTANT_String_info 引用UTF8常量值
 * CONSTNAT_Class_info 引用UTF8常量值  
 * CONSNAT_NameAndType_info 引用两个UTF8常量值，用":"间隔。如：#3 = #1:#2
 * CONSNAT_Methodref_info 引用两个UTF8常量值，同"."间隔。如：#4.#3
   + 组成是Class引用.NameAndType引用
   
### 属性
 * attribute_info
 * java语法结构中其它信息都是由属性来表示。
 
### 动态编译java源代码
 * 静态编译：通过java编码器来完成，编码与运行是两个独立过程。
 * 动态编译：编译与运行两个过程统一在运行时完成
   + javac 
     - 通过ProcessBuilder创建进程，执行javac命令。缺点输入，输出只能是文件。性能低
     - 通过javac API, com.sun.tools.javac.Main.compile, 优点：移植性好，性能更优。
   + java编译API
     - javax.tools.JavaCompiler
     - 优势在于java源代码的存在不限于文件形式
   + eclipse JDT
   
### 字节码增强
 * 在没有源代码，只有字节码时，修改字节码内容来改变程序执行。
 * 程序运行前增强（jvm启动前）
   + 先修改字节码，然后由虚拟机运行。通过工具修改
   + 个人开发使用
   + 避免运行时修改带来的开销
   
 * 程序运行时增强（jvm运行时）
   + 在字节码加载之前对字节码修改。由增强代理或类加载器完成
   + 框架开发使用，只能在运行时获取字节码
   
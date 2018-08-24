## java语言规范

### 文法
 * 文法由多个句法组成
 * 句法
   + 非终结符加冒号 + 产生式
   + 
 * 产生式
   + x{, x}, 表示0至多个x
   + [x], 表示0或1个x
   + 一个产生式在一行写不下时，使用换行加缩进实现多行。
   + 如果非终结符加冒号后加 one of 则产生式表示可选成员
   + 产生式+ but not 表示某些扩展是不允许的
   + 其它说明，如any unicode character
      

### 词法结构

#### 词法翻译
 * java文件是用unicode字符编写，通过分析unicode字符流来分析程序
 * java文件是按行处理
 * 从unicode字符流中分析出一行内容，通过判断行终止符
 * 输入元素序列：空白字符，注释，符号
 * 符号组成
   + 标识符
   + 关键字
   + 字面常量
   + 分隔符
   + 操作符
 * 翻译过程
   1. 把原始unicode字符流中的Unicode转义字符(即\u开头的), 翻译成相应的unicode字符
   2. 将1步产生的unicode流翻译成由输入字符和行终止符构成的流
   3. 将2步产生的流翻译成输入元素构成序列，其中空白字符和注释被摒弃。这些输入元素由符号组成。
   
#### java空白字符
 * 空格符
 * 水平制表符
 * 换页符
 * 行终止符

#### java行终止符, LineTerminator
 * CR
 * LF
 * CRLF
 
#### 字面常量
 * 浮点数
   + 组成：整数部分，小数部分，指数，以及类型后缀。
   + 十近制表示：指数用e/E表示，后面加一个可选的有符号整数，123E+12, 123E-12
   + 十六控制表示：指数用p/P表示，后面加一个可选的有符号整数
   
 * 字符字面常量
   + 单引号一个字符
   + 包括：一个字符，或一个EscapeSequence中的字符
   + 不能使用', \
   + 不能使用\u000a(换行), \u000d(回车)， 会在翻译过程第3部过滤掉，应该使用\r, \n(EscapeSequence形式)
 
 * 字符串字面常量
   + 双引号多个字符
   + 字符串和EscapeSequence
   + 不能使用", \
   + 不能使用\u000a, \u000d, \u0022(单个双引号)

#### 字符与字符串字面常量中的转义序列- EscapeSequence
 * 跟在转义序列中的反斜杠后面的字符可以是
   + b, 即\b, 退格
   + t, 即\t, 水平制表符
   + n, 即\n, 换行 
   + f, 即\f, 换页
   + r, 即\r, 回车
   + ", 即\", 双引号
   + ', 即\', 单引号
   + \, 即\\, 返斜杠
   + 0-7
#### java分隔符
 * (), [], {}, ;, ,, ., @, ::, ...
 
 
### 类型，值和变量
 
#### 浮点数
 * 除了NaN，浮点数都是有序的。
   + 负无穷，负有穷非0值，正0和负0， 正有穷非0值，正无穷
   + (负无穷)NEGATIVE_INFINITY(-1.0f/0.0f) 
   + (正无穷)POSITIVE_INFINITY(1.0f/0.0f)
 * 0.0f == -0.0f 结果是true, 0.0f > -0.0f 结果是false
 * NaN
   + NaN = 0.0f / 0.0f;
   + 如果操作数中有一个是NaN, 则数字比较操作符<, <=, >, >= 返回false
   + 如果操作数中有一个是NAN, 则判等操作符==返回false
   + 如果操作数中有一个是NaN, 则不等操作符!=返回true; (x=NaN, x != x 返回true)
   + 如果操作数中有一个是NaN, 则任何数值运算都返回NaN;
   
### 类型转换与上下文
 
#### 拓宽简单类型转换
 * byte到short, int, long, float, double
 * short到int, long, float, double
 * char到int, long, float, double
 * int, long 转float， 或long转double会丢失精度
 * byte不能到char, short不能到char, char不能到short
   + 只能通过强制类型转换
 * byte, short, char 之间的转换都是通过先转为int再转为相应类型
   
#### 窄化简单类型转换
 * short->, b, c
 * char->, b, s, 
 * int->, b, s, c
 * long->, b, s, c, i
 * float->, b, s, c, i, j 
 * double->, b, s, c, i, j, f
 * 浮点数NaN转为int, long的0
 * 浮点数其它采用向0舍入，如果表示太小用int, long的最小值， 表示太大用Int, long的最大值
 
#### 非受检转换
 * 原生类或接口类型G到任意泛型类型G<T..N>的非受检转换
 * 原生数组类型G[]到任意泛型类型G<T..N>[]N的非受检转换
 * 非受榉转换会导致编译时非受检警告， 除非泛型是G<?>无界通过符类型，或添加SuppressWarning注解
 
#### 参数化类型
 * ? extends B, 只能取
    ```
       /**
         * 泛型上界, 只能取，不能放
         * 上界是相对于引用来说的：如list = aBeans, list = bBeans. CommonPti是ABean, BBean的上界
         * 引用传递只能是越传越大，即子类引用向父类引用传递。保证取出的数据不会出错
         * 为什么不能放
         * 1. list = aBeans时，此时引用为List<? extends CommonPti> 类型， 如果放BBean(), 则出错；
         * 为什么可以取, 上界相对于 ? extends T 和来说
         * 1. 因为list = aBeans, list = bBeans取出来都是CommonPti
         *
         * 结果上界就是为了解决多态会产生的类型不正确问题
         */
    ```
 * ? super B, 可以放B与B的子类，也可以取。因为任何一个类可能实现了B,C,D, 放的时候只要实现B, 取的时候可能是C,D.
    ```
      /**
         * 泛型下界, 能放，能取
         * 下界是相对于引用来说的：如aBeans = list, bBeans = list. CommonPti, ABean, BBean指定界
         * 引用传递只能是越传越小，即父类引用向子类引用传递。保证放进去的数据不会出错。
         * 下界也相对于 super T， 相对于T来说，放的至少是T
         *
         * 为什么可以放
         * 1. aBeans = list, bBeans = list, list不管由谁引用，放进去的至少是CommonPti
         * 为什么可以取
         * 1. 如果按与上界对应关系，则是不可以取的
         * 2. 因为没有对取做限制，现在是可以取。也是很容易出错。只能确定类型，然后再强制转换
         */
    ```
 * 引用转换
   + T <: S, 表示T是S的子类型
   + S :> T, 表示S是T的真超类型
   + ? == ? extends Object
   
   + 如果T <: S, 则? extends T <= ? extends S. 即：List<? extends S> = List<? extends T>
   + ? extends T <= ?. 即：List<?> = List<? extends T>
   + 如果S <: T, 则? super T <= ? super S. 即：List<? super S> = List<? super T>
   + ? super T <= ?. 即：List<?> = List<? super T>
   + ? super T <= ? extends Object. 即：List<? extends Object> = List<? super T>
   + ? extends T <= ? extends Object. 即：List<? extends Object> = List<? extends T>
   + T <= T 即：List<T> = List<T>
   + T <= ? extends T 即：List<? extends T1> = List<T2>, 其中 T2 <: T1 
   + T <= ? super T 即: List<? super T1> = List<T2> , 其中 T1 <: T2 
   
#### 窄化引用类型转换
 * Object, 接口Clonable, Serializable转换为任意的数组类型T[]
   + Clonable c = new int[1]; //成立
 
#### 捕获转换
 * 应用于通配符类型
 * 捕获转换不能递归使用
 * ? -> T, List<?> -> List<T>
 * ? extends B -> T, List<?extends B> -> List<T>
 * ? super B -> T, List<? super B> -> List<T>
   
   
#### 方法调用上下文
 * 不允许数值类型表达式窄化转换
 ```
  static void done() {
     m(12, 2); //编译时error, 隐式的窄化转换
  }
  static double m(byte a, int b) {
          return a + b;
      }
 ```
 
### 明确赋值
#### 局部变量和final变量必需在使用前，定义
 ```
  //可以编译通过，编译器可以判断出k肯定会被使用前赋值
  void flow() {
    int k;
    while(true) {
      k = 3;
      syso(k);
      if(k == 5) {
        break;
      }
      
      k = 5; 
    }
  }
  // 编译失败，因为编译器只能实别常量表达式，n>2不是常量表示达，所以不能保证k被赋值。
  //常量表示式：简单类型与string构成。如：3>2
  void flow() {
    int k;
    int n = 5;
    if(n >2) {
      k = 3;
    }
  }
 ```
### 第4章


 * 当您在一个源文件中定义函数且在另一个文件中调用函数时，函数声明是必需的。在这种情况下，您应该在调用函数的文件顶部声明函数。
   


### 函数
 * 如果函数定义中省略了返回值的类型时，则默认为int类型
 * 由于 C 语言不允许在一个函数中定义其它函数，因此函数本身是“外部的”。
 
### 采用逆波兰表示法
 * 1 3 + 2 5 - * = (1+3) * (2 - 5) 
 
 
### 预处理器
 * 宏使用
   + 使用宏变量的地方会直接替换为宏的值，就像java中的static final 类型变量，在编译期被替换
   
 * 宏函数使用
   + 使用的地方直接替换为对应函数体代码，这样就避免了每次执行都进行函数调用的开销
   
 
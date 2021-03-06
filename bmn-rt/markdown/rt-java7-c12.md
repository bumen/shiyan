## java泛型

### 类型
 * TypeVariable 泛型变量 E
 * GenericArrayType 泛型数组 E[]
 * ParameterizedType 参数化类型 <E>
 * WildcardType 通配符类型

### 定义
 * ObjectHolder<T> 是泛型类型，<T>为形式类型参数
 * 泛型类型：在一个类型中使用了形式类型参数，称为该类型为泛型类型
 * 泛型类型实例化
   + 泛型类型声明的形式类型参数被替换成实际类型。
 * 参数化类型：用具体类型代替形式类型参数， 一般实例化后时指定参数化类型, 或实现类或接口时指定
   + 如：new ObjectHolder<String>
   + 一个泛型类型，可能的参数化类型数量非常多。
   + 通常分为：不带通配符的类型，与带通配符的类型。
  
 * 枚举、匿名内部类、异常类型不可以使用形式类型参数，不能成为泛型类。
 
 * 形式类型参数
   + 不能用来创建对象和数据
      - new T() , new T[]
   + 不能作为父类型
      - class MyClass extends T
   + 不能使用instanceof
      - instanceof T
   + 不能使用类型字面量
      - T.class
   + 不能出现在异常处理中
      - catch(T)
   + 不能出现在静态上下文中
      - static T
      
 * 原始类型：
   + 泛型类型在使用时不指定实际类型而直接使用类型声明，所得到的类型称为原始类型
   + new ObjectHolder();  //是泛型类型，但直接使用类型声明
   
 * 泛型方法与泛型类
   + 泛型方法与泛型类没有直接的关系
   + 一个非泛型类中可以包含泛型方法
   + 泛型类型中的泛型方法可以使用在类型中定义的形式类型参数，也可以使用自己的形式类型参数。
   + 泛型方法使用时，可以不需要显示指定所用的实际类型。编译器根据方法调用时的实际参数类型和上下文信息进行类型推断。
   
 * 可具体化类型
   + 在运行时可用的类型被称为可具体化类型
   + 包括非泛型类型
      - 如: String
   + 包括所有实际类型都是无界通配符的参数化类型
      - 如: List<?>
   + 包括原始类型
      - 如: List
   + 包括基本类型
      - 如: int
   + 包括元素类型为可具体化的数组类型
      - 如: String[], List<?>[], List[], int[]
   + 包括父类型和自身都是可具体化类型的嵌套类型
      - 如: MyClass<?>.Inner
   + 除了实际类型是无界通配符的参数化类型外，java泛型中几乎所有参数化类型都是不可具体化的。
      - 如: List<String>, List<? extends Number> 都是不可具体化的
   + 虚拟机在执行字节代码时只能使用运行时可用的可具体化类型。
   
     > 如异常为例：java运行时的异常捕获和处理是由虚拟机来完成的。因此异常类型必须是可具体化的
   
### 类型擦除
 * 擦除泛型类和泛型方法中声明的形式类型参数，以及参数化类型中的实际类型信息。
 * 擦除形式类型参数
   + 泛型类型声明中的部分被直接删除
     - ObjectHolder<T> 替换成ObjectHolder
   + 泛型类型代码中出现的，则根据上界替换成具体类型
     - 如果形式类型参数声明了上界，则声明中最左边的上界作为进行替换的类型
     - 如果形式类型参数声明没上界，则使用Object类进行替换
 * 擦除参数化类型的实际类型参数
   + 直接删除
 * 擦除后可能会出现代码逻辑不合法情况，编译器会通过插入适当的强制类型转换和生成桥接方法来解决。
 
### 通配符类型
 * 通配符代表一组类型，而不是单一具体类型。
 
 * 通配符类型无法与单个具体类型兼容。只能与其它通配符类型兼容。
   + 如： List<? extends Number> 与 List<Integer>, 不兼容。
   
### 数组
 * 数组是协变的
   + String[] 是 Object[] 的子类
   
 * 只有可具体化的类型，才能创建数组对象
   + Class<?>[] 是可以的，只有无界通配符类型才可以创建数组
 * 可变长度的泛型数组，通过@SafeVarargs 消除警告
   + void say(List<String>... args)
   
### 类型系统(泛型)
 * 类型系统描述不同类型之间的转换关系
 * 两个维度
   + 一个是泛型类型，二个是参数化类型
 * 当参数化类型相同时，两个类型的父子关系取决于泛型类型本身的父子关系
   + 如：List<Number> 是 ArrayList<Number> 父。
   + 通配符类型也同样如此
   + 如: List<? extends Number> 是 ArrayList< ? extends Number> 父。
 * 当泛型类型相同时，实例化时不是包含通配符的两个不同具体类型，它们之间不存在父子关系
   + 如: List<Number> 与 List<Integer>
 * 当泛型类型相同时，父子关系只存于包含通配符的情况，至少要有一个类型包含通配符
   + 无界通配符是所有的父类型
   + 有上界，则判断两个上界是否存在父子关系
     -  \* extends A, * extends B , A extends B. 则 * extends B 是 * extends A 的父
   + 有下界，则判断两个下界是否存在父子关系
     - 这个与上界正好相反
     - ? super A, ? super B, A extends B. 则 ? super B 是 ? super A的子
     - List<? super A> listA, List<? super B> listB. 则 listA = listB
   + 多次使用通配符时，使用从最内层类型开始判断比较
     - List<? extends List<Integer>> List<? extends List<? extends Number>>
     - Integer 是 ? extends Number 是子类型，则List<Integer> 是 List<? extends Number>子类型
     - 而这两个分别是通配符类型的上界，则根据上界关系规则
     
   + 传递关系
     - ArrayList<Integer> 与 Collection<? extends Number>
     - ArrayList<Integer> 是 Collection<Integer> 是子类
     - Collection<Integer> 是 Collection<? extends Number> 子类
     - 所以ArrayList<Integer> 是Collection<? extends Number> 子类
     
 * super 可以放，不能取
   + ? super A. 只能放任何继承A的对象
   + 如：
      - A, B, C, D类。 A extends B, C extends B, D extends A
      - List<? super A> listA, List<? super B> listB
      - listB.add(A), listB.add(C) 成功
      - listA.add(A)
      - listA = listB
      - listA.add(D) 可以。
      - 因为listB中有C, 但是listA不能放C. 所以super不支持取
      
 * extends 可以取，不能放
   
### 泛型类继承关系
 * 方法签名：方法名与参数
 * 方法类型签名：是参数组成
 * 覆写
   + 返回值，方法签名，异常声明必须一样
     + 返回值，子类方法返回值类型必须可以替代父类型中对应方法的返回值
   + 如果父类参数化类型方法，再类型被擦除后与子类中的方法签名一样。说明子类方法覆写父类方法
   + 如果只满足方法签名一样，但是返回值或异常不满足时，则会编译出错
 * 重载
   + 方法名相同
   + 只会考虑类型签名不相同
   
   
 * 父类非泛型类，子类非泛型类
   + 普通方法覆写
   + 子类普通方法覆写父类泛型方法
     - 可以，普通方法签名与父类泛型方法擦除后方法签名一样
   + 子类泛型方法覆写父类泛型方法
     - 可以，当父类方法再类型擦除后与子类方法一致时
   + 子类泛型方法覆写父类普通方法
     - 不可以
 * 父类非泛型类，子类泛型类
   + 子类方法使用是类型定义时声明的形式类型参数，则不能覆盖父子非泛型方法
 * 父类泛型类，子类非泛型类
   + 子类可以覆写，父类方法
   + 子类泛型方法，不能覆写父类中使用类型定义时的形式类型参数的方法。
 * 父子都为泛型类型
   + 可以覆写
   
### 类型推导
 * 优先使用参数类型，如果没有参数，则使用返回值类型
 * 对象构造时的类型推导
   + 优先使用构造函数实际参数的静态类型
   + 没有参数，则使用对象引用的静态类型
   
### 通配符捕获
 * 通过捕获助手
 ``` 
    // 通配符捕获
    public void rebox(Box<?> box) {
        reboxHelper(box);
    }
    
    // 捕获助手
    private<V> void reboxHelper(Box<V> box) {
        box.put(box.get());
    }
 ```
## Generic 泛型

### extends, 上边界
 * <T extends A & B & C>语法来声明，
 其中只能有一个是类，并且只能是extends后面的第一个为类，其他的均只能为接口(和类/接口中的extends意义不同)。
 * 使用extends关键字指定泛型实例化参数只能是指定类的子类
 
### super, 下边界
 * super关键字可以指定泛型实例化时的参数只能是指定类的父类

### 通过符：？


### Type
 * Class
 * ParameteriedType
 * GenericArrayType
 * WildcardType
 * TypeVariable
 * 如果一个方法返回Type类型或需要Type类型参数时，就可以判断是否是上面5种类型
   + 通过 instancesof判断
 

### GenericDeclaration
 * 可以声明泛型的位置
 * Class, Method, Constructor
 * 只有一个方法getTypeParameters
   + 所以Class, Method, Constructor
   + 只能定义TypeVariable类型的的泛型，即T，泛型变量
   
   
### class SelfBounded<T extends SelfBounded<T>>
 * 自绑定
   + 泛型的自绑定约束目的是用于强制继承关系，即使用泛型参数的类的基类是相同的，强制所有人使用相同的方式使用参数基类。
## Class

 * 反射，主要就是Type, Annotation
 * Type： 主要有类与泛型
 * Annotation： 主要是AnnotationElement

### getMethds()
 * 返回所有Public 方法， 包括接口与父类

### getDeclaredMethods()
 * 返回所有当前类声明的方法，private public protected default
 
### isInterface()
 * 只有是interface 定义的返回true
 
### isAssignableFrom
 * Parent.class.isAssignableFrom(Child.class) 返回 true
 * Child类是否可以指向Parent向， 可以向上转型
 
### getTypeParameters
 * 类，只能定义泛型变量 TypeVariable
 * 返回类定义的所有TypeVariable
 * 声明一个class时，如果带范型，则只能是参数化类型。不能带具体参数类型
 
### getSuperclass
 * 如果是class ， 则返回所有extends class父类, 最高Object
 * 如果是interface, 没有superclass
 
### getGenericSuperclass
 * 返回可以带泛型的类型 Type
 * 如果是泛型，则返回ParameterizedType
   + 参数可以是具体参数类型 如果 extends Super<String>, 则返回Super<String>
   + 也可以是参数化类型 如果 extends Super<T>, 则返回Super<T>
 * 如果不是泛型，则返回Class<T>
 
### getInterfaces
 * 如果class, 则只返回当前类implements 的接口
 * 如果interface, 测返回 extends 的接口
 * 如果是primitive, void 返回 length 0 array
 
### getGenericInterfaces
 * 会返回所有实现的接口
 * 只是如果接口带泛型，则可以获取泛型信息
 * 与getGenericSuperClass 返回一样
 
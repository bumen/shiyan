## Generic 泛型


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
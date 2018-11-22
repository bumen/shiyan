## core

### Ordered接口
 * order排序按升序排列：越小越靠前
 * PriorityOrdered：比普通Ordered, 优先级高
 
### spring.property文件加载
 * SpringProperties
 
### MethodParameter 方法与参数解析类
 * 集成
   + 参数类型：可以手动指定，如果没有指定，则通过方法获取参数类型
   + 带范型的参数类型
   + 参数annotation
   + 参数名
   + 参数索引
   + 参数所属的方法 method或constructor
 * 可以获取方法的annotation
   
 * 方法包括
   + read method，此parameter为-1。没有参数
   + write method, 此parameter大于=0。
   + constructor method, 此parameter大于=0
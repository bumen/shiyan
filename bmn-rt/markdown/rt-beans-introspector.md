## introspector
 * 是BeanInfo的解析工具类
 * 查找过程，先查找父类，再查找子类。最后把父类beaninfo合并到子类beaninfo, 返回子类beaninfo
 * 有三个主要bean
   + explicitBeanInfo ---上次已经解析过的当前类的beanInfo
   + superBeanInfo------当前类的父类的beanInfo
   + additionalBeanInfo[]
 * 有三个主要集合
   + methods[] - MethodDescriptor
   + properties[] - PropertyDescriptor
   + events[]   - EventSetDescriptor  
   

### BeanInfo搜索路径 
 1. 查找当前BeanClass 所在包
 2. 查找配置路径
 > 每当新建一个BeanInfo。如果是USE_ALL_BEANINFO, 
 > 则会保存到BeanInfo搜索路径下，供下次使用。不用新建
 
### BeanInof Tag
 1. USE_ALL_BEANINFO 
   * 当前类与所有父类如果再BeanInfo搜索路径查找，如果已经有了，就使用。否则新建
 2. IGNORE_IMMEDIATE_BEANINFO
   * 当前类，新建。所有父亲再BeanInfo搜索路径中查找，如果有了就返回，否则新建
 3. IGNORE_ALL_BEANINFO
   * 当前类，所有父类，都新建

### 解析BeanInfo
 * 都是通过方法来获取信息
 * ​获取BeanDescriptor
 * MethodDescriptor[]
 * EventSetDescriptor[]
 * PropertyDescriptor[]

### getTargetMethodInfo
 * 获取所有方法MethodDescriptor
 * 所有子类与父类方法
 
### getTargetPropertyInfo
 * 获取所有属性PropertyDescriptor
 * 所有子类与父类属性
 * 通过get, set, is方法获取属性内容
 * 非静态方法
 * get方法
   + 没有参数
 * is 方法
   + 没有参数
   + 返回值为boolean
 * get方法
   + 有一个参数， 且是int类型
   + IndexedPropertyDescriptor
 * set方法
   + 有一个参数
   + 返回值为void
 * set方法
   + 有两个参数
   + 第一个参数为int
   + 返回值为void
   + IndexedPropertyDescriptor
 
### getTargetEventInfo
 * 获取所有bean 中 event
 * 查询过程
   1. explicitBeanInfo 获取
   2. superBeanInfo 获取
   3. additionalBeanInfo 获取
   4. beanClass 获取， 所有public 非静态 方法 
     + 必须是add, remove, get开头的方法
     + add方法过滤-规则
       - 返回值为void
       - 只有一个参数
       - 参数是java.util.EventListener 的子类
       - add方法名后几位，必须与参数类型名称的结尾匹配
       - 如： 参数类型为RtBeanEventListener, 则add方法有 
       - addRtBeanEventListener, addEventListener 等
     + remove方法过滤-规则
       - 返回值为void
       - 只有一个参数
       - 参数是java.util.EventListener 的子类
       - 与add方法后几位匹配规则一样
     + get方法过滤 -规则
       - 没有参数
       - 返回值是数组
       - 方法是代复数形式，如getEventListeners
       - 数组类型是java.util.EventListener 的子类
       - 与add方法后几位匹配规则一样, 取方法名的EventListener， 去掉s
     + 必须有add，与remove方法
     + 且有add方法，同时有同名remove方法 && add方法的结尾是Listener结尾
     + 找到add方法参数类
     + 获取参数类中所有方法，方法参数是EventObject子类的方法。为事件回调方法
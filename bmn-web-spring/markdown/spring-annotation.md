## spring中的Annotation

### core-annotation包
 * @AliasFor
   + 注解到annotation类中的方法上。成对出现
   + 表示一个属性是另一个属性的别名。然后使用时，这两个属性只能使用其中一个
   + 约束判断：注解了@AliasFor的注解会返回一个Jdk动态代理类。在调用这个注解的方法时通过jdk动态代理判断是否违反约束。
   + 尽量别用这个属性，因为会通过jdk动态代理。使用过程中还会有更多的逻辑判断，影响性能
   
 * @Order
   + 用于判断接口调用顺序。
   + 顺序值越小，越靠前
 * PriorityOrdered(class)
## annotation
 * 元注解
    + Documented
    + Inherited
    + Native
    + Repeatable
    + Retention
    + Target 
 
### AnnotatedElement 代表注解的元素
  * AnnotatedElement
    + 实现它的接口的地方，可以声明注解
    + 类，方法，方法参数，构造器，字段，E(范型变量)，注解上
   
  * AnnotatedElement 可以获取该位置上的所有注解
  
### Annotation 注解接口
 * 是所有通过 @interface定义的注解的父类
 * 通过annotationType方法获取 @interface注解类型
   + 因为annotationType返回一个Class<?>类型对象，所以通过getAnnotation可以返回定义的元注解
  

### @Inherited
 * 通过再注解上添加这个元注解。来实现注解的间接继承
 
 * 如:
```
    @Inherited
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Name {
    }
    
    @Name
    Class A {}
    
    Class B extends A {}
    
    B.class.getAnnotations() 可以返回 Name
    
    如果Name注解没有Inherited， 则从B.class中拿不到Name注解

```

### java8 添加 元注解@Repeatable
 * 在同一个位置重复相同的注解
 
 * Java8还在AnnotatedElement接口新增了
   + getDeclaredAnnotationsByType
   + getAnnotationsByType
 
    ```
    两个方法并在接口给出了默认实现，在指定@Repeatable的注解时，
    可以通过这两个方法获取到注解相关信息。
    但请注意，旧版API中的getDeclaredAnnotation()和 getAnnotation()是不对@Repeatable注解的处理的(除非该注解没有在同一个声明上重复出现)。
    注意getDeclaredAnnotationsByType方法获取到的注解不包括父类，
    其实当 getAnnotationsByType()方法调用时，
    其内部先执行了getDeclaredAnnotationsByType方法，
    只有当前类不存在指定注解时，getAnnotationsByType()才会继续从其父类寻找
    ```
 * 使用
    ```
       @Repeatable(value = Roles.class)  
        public static @interface Role {  
            String name() default "doctor";  
        }  
      
        @Target(ElementType.TYPE)  
        @Retention(RetentionPolicy.RUNTIME)  
        public static @interface Roles {  
            Role[] value();  
        }  
          
        @Role(name = "doctor")  
        @Role(name = "who")  
        public static class RepeatAnn{  
              
        }  
          
        @Roles({@Role(name="doctor"),  
                @Role(name="who")})  
        public static class Annotations{  
              
        }  
    
    
    ```
    
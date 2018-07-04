## java类加载器

### 初始化类加载器，定义类加载器
 * 初始化类加载器是loadClass方法加载的
 * 定义类加载器是defineClass方法产生的
 * 两个可以是不同的类加载器
 
### 自定义类加载器
 * defineClass是 final类型的不需要重写
 * 可以重写loadClass, findLoadedClass, findClass
 * loadClass
   + 封装了默认的双亲类加载器优先的代理模式
   + 如果要改变默认的双亲优先代理模式，则重写这个方法
 * findLoadedClass
   + 虚拟机会记录下已经加载的类的初始类加载器
   + 方法实现：会查找相同初始类加载器与类名相同，则返回true
 * findClass
   + 主要用来封装当前类加载器对象自己的类加载逻辑。
   
### 类加载器的隔离作用
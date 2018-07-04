## java7新特性

### 语法
 1. switch 可以使用字符串
   * 只做了编译器一级的修改
     + 一个case 转为 if
     + 一个case 一个 default 转为 if else
     + 其它，case为字符串的hashCode. 然后使用 if (s.equalis('xxx'))
   
   
 2. 字面值 
   * 默认十进制
   * 0 八进制
   * 0x 十六进制
   * 0b 二进制
   * 数据可以使用 **_**
     + 123_455 = 123455
     + 5_6.3_3 = 56.33
     
 3. 异常优化
   * catch 可以同时捕获多个异常
     + 通过 | 实现多个异常
     + 不能出现重复类型
     + 也不允许子类与父类
     + 实现：通过编译器，编译成多个catch实现
    
   * 在捕获异常并重新抛出异常时的异常类型更加精确  
     + java7之前，这种可以被编译器，编译通过，但不精确
   
      ```
        public void say() throws ExceptionA {
          try {
             throw new ExceptionASubB();
          } catch( ExceptionA e) {
              try {
                  throw e;  //未修改直接又抛出
              } catch(ExceptionASubA a) {  //这段try-catch可以上次接口调用say(), 编译错误
              }
          }
        }
      ```
     + java7 
        - 如果一个 catch 子句的异常类型参数在 catch 代码块中没有被修改， 而这个异常又被重新抛出，
        - java编译器可以准确知道e 是SubB , 所以第二个catch再捕获SubA时，是不可能的。
        
   * 目前的主流意见是， 最好优先使用非受检异常。
      * 异常声明是 API 的一部分
        + 在一个公开方法的声明中使用 throws 关键词来声明其可能抛出的异常的时候， 这些异常就成为这个公开方法的一部分， 属于开放 API。
        + 所以要考虑向后兼容性
        + 因为API使用者肯定已经使用了try-catch-finally, 如果后面发现这个异常要删除，会导致使用编译出错
        + 所以一但声明受检异常，很长一段时间就甩不掉了。
        + 所以优先使用非受检异常。但要有文档说明
        
   * 自定义异常
      + 异常层次结构
         - 与程序本身的类层次结构相对应
         - 低层向上层抛出时，使用包装异常
         
   * 处理异常
      + 如果某个异常在当前的调用栈层次上是可以处理和应该处理的，那么就应该直接处理掉
      + 如果当前的代码位于抽象层次的边界，就需要首先捕获该异常，重新包装之后， 再往上传递。
      
   * 消失的异常-被抑制
      + try-catch-finally
      + 当try中抛出异常后，如果finally中也抛出异常。则最后只能看到finally里的异常， try抛出的异常消失了
      + 解决消失的异常
        1. java7 之前, 使用中间变量， 只关心try抛出的异常
        ```
          IOException ex = null;
          try {
          
          } catch(IOException e) {
              ex = e;
          } finally {
              try {
              
              } catch(IOException t) {
                  if( ex == null) {
                      ex = t;
                  }
              }
              
              if(ex != null) {
                  throw new BaseException(ex);
              }
          }
        ```
        2. java7 使用addSuppressed
        ```
          IOException ex = null;
          try {
          
          } catch(IOException e) {
              ex = e;
          } finally {
              try {
              
              } catch(IOException t) {
                  if( ex != null) {
                      ex.addSuppressed(t);
                  } else {
                      ex = t;
                  }
              }
              
              if(ex != null) {
                  throw ex;
              }
          }
        ```
        
   * try-with-resource 优化资源释放
      + 只有实现java.lang.AutoCloseale接口的类，才可以使用
      + 还可以同时，管理多个资源
   
     ```
      try(BufferedReader reader = new BufferedReader(new FileReader(name))) {
                  
      } catch (IOException e) {
          e.printStackTrace();
      }
     ```
     >如果资源初始化时或 try 语句中出现异常， 而释放资源的操作正常执
      行， try 语句中的异常会被抛出 ； 如果 try 语句和释放资源都出现了异常， 那么最终抛出
      的异常是 try 语句中出现的异常， 在释放资源时出现的异常会作为被抑制的异常添加进
      去， 即通过 Throwable.addSuppressed 方法来实现。
      
     
 4. 泛型可变参数
   * @SafeVarargs， 消除警告
   * 方法必须是 static 或 final， 否则编译错误 
   

### java语言动态性
 1. 脚本
   * javax.script.ScriptEngineManager() 脚本引擎管理器
   
 2. 语言绑定
   * java与其它脚本引擎交互
   * 数据传递通过语言绑定对象来完成， 其实就是一个简单hash表。用于存放和获取共享数据。
     + javax.script.Bindings 定义了语言绑定对象的接口
     + createBindings 或 new SimpleBindings 自定义语言绑定对象
     + ScriptEngine put get
   
 3. 脚本执行上下文
   * ScriptContext
   * 修改输入，输出 (默认都到控制台)
   * 自定义属性，有作用域（getScopes）
   
 4. 脚本编译
   * 脚本语言一般是解释执行，当多次执行同一段代码时，效率不高。
   * 如果脚本引擎支持编译，会实现javax.script.Compilable 
   * 通过 ScpritEngine instanceof Comilable 来判断是否支持编译
   * 通过 CompiledScript 来编译执行
   
 5. 方法调用 
   * 有些脚本引擎允许使用者单独调用脚本中的某个方法。
     + 脚本引擎可以实现 javax.script.Invocable 接口
     + ScriptEngine 对于 Invocable 接口的实现也是可选的。
     + 通过 Invocable 接口可以调用脚本中的顶层方法， 也可以调用对象中的成员方法
     + 可以通过 Invocable 接口中的方法来获取脚本中相应的 Java 接口的实现对象。 这样就可以在 Java 语言中定义接口，在脚本中实现接口。
     
     
 6. java反射API
   * 获取构造器
     + 获取嵌套的构造器，判断，static嵌套类，还是非static嵌套类
     + static 与外部类一样获取
     + 非static 需要第一个参数是外部类的Class<T> 对象
     
   * 获取属性
     + static 属性， obj属性
     + static 属性时，set(null, value);
     + obj 属性时，set(this, value);
     
   * 操作数组
     + java.lang.reflect.Array, 实现  
     
   * 异常
     + 异常的 getCause 方法可以获取到真正的异常信息， 帮助进行调试。
     + java7之前要捕获不同的反射
     + java&之后反射异常有一个共同父类ReflectiveOperationException
     
   * 动态代理
     + 静态代理
        - 缺点：需要实现接口，当接口修改是，代理类也要修改
     + jdk动态代理
        - 缺点: 被代理的类需要实现接口
        - 代理多个接口时，如果接口中有重名方法，则出现在前的接口方法有效
     + cglib动态代理
        - 不需要实现接口
        
### 动态语言支持
 * 方法句柄
   + 对于一个方法句柄来说， 它的类型完全由它的参数类型和返回值类型来确定， 而与
     它所引用的底层方法的名称和所在的类没有关系。
     
   + MethodType
     ```
      1. 不可变对象
      2. 由返回值类型和参数类型来确定
      3. 第一个参数是返回值类型，后面是参数类型
      4. wrap 和 unwrap 用来在基本类型及其包装类型之间进行转换
      5. generic方法把所有返回值和参数类型都变成 Object 类型， 而 erase 只把引用类型变成 Object，
         并不处理基本类型
     ```
     
   + MethodHandle 方法句柄
     ```
      1. invokeExact 方法在调用的时候要求严格的类型匹配，方法的返回值类型也是在
                                       考虑范围之内的。
      2. 因此在使用 invokeExact 方法进行调用时， 需要在前面加上强制类型转换， 以声
         明返回值的类型。
      3. 如果去掉这个类型转换， 而直接赋值给一个 Object 类型的变量， 在
         调用的时候会抛出异常， 因为 invokeExact 会认为方法的返回值类型是 Object。 
      4. 去掉类
         型转换但是不进行赋值操作也是错误的， 因为 invokeExact 会认为方法的返回值类型是
         void， 也不同于方法句柄要求的 String 类型的返回值。
                                       
     ```
     
     - invoke 方法允许更加松散的
       ```
        1. 调用方式。 它会尝试在调用的时候进行返回值和参数类型的转换工作。
        2. 如果其声明的类型，与实际调用时的类型不匹配，invoke 会先调
                                 用 asType 方法来尝试适配到调用时的类型。 
                                 如果适配成功， 调用可以继续； 否则会抛出
       ```
       
     - 可变参数方法使用
     
     - 动态bind执行对象
       ```
        1. bindTo, 可以绑定多个参数
        2. 在进行参数绑定的时候， 只能对引用类型的参数进行绑定
        3. 无法为 int 和 float 这样的基本类型绑定值。 
       ```
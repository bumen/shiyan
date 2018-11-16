## java8 API

### 新特性使用

#### lambada

#### defulat 接口

#### Optional<T>

#### Instant

#### HashMap

#### Files- 是java7的特性
 * https://www.ibm.com/developerworks/cn/java/j-nio2-2/index.html


### java.io

### java.security
 * jca

### java.util.prefs
 * 

### java.util.stream
 * java8加的流

### javax.accessibility 
 * JAAPI

### javax.activation
 * JAF(JavaBeans Activation Framework)

### javax.activity
 * 包含解组期间通过 ORB 机制抛出的与 Activity 服务有关的异常。

### javax.annotation.processing 
 * 编译器注解

### javax.crypto 
 * java安全扩展

### javax.imageio 
 * 图片处理

### javax.jws 
 * java自带，轻量级webservice服务

### javax.lang.model
 * javax.lang.model用来为 Java 编程语言建立模型的包的类和层次结构。 此包及其子包的成员适用于语言建模、语言处理任务和 API（包括但并不仅限于注释处理框架）。

### javax.management 
 * java虚拟机管理器
 
### javax.net
 * java 套接字扩展
 
### javax.naming
 * Java Naming and Directory InterfaceTM (JNDI)

### javax.print
 * 打印机功能相关
 
### javax.rmi 
 * java.rmi 的扩展
 * 用于不同虚拟机之间的通信，这些虚拟机可以在不同的主机上、也可以在同一个主机上；一个虚拟机中的对象调用另一个虚拟上中的对象的方法，只不过是允许被远程调用的对象要通过一些标志加以标识
 * stub 是存根。是远程对象的本地引用供本地方法调用

### javax.script 
 * 脚本语言支持

### javax.security
 * java安全扩展

### javax.sql
 * 包括了jdbc3.0的特性   
 * 提供了很多新特性,是对java.sql的补充,具体提供了一下方面的功能

### javax.tools
 * 可以实现 Java 源代码编译，使您能够添加动态功能来扩展静态应用程序。
 * java动态编译器抽象

### javax.transaction 
 * JTA 事物
 * javax.transaction.xa
   + 分布式事物
   
### javax.xml
 * javax.xml.bind
   + jaxb
 * javax.xml.crypto
   + 对xml文件进行数字签名
 * javax.xml.parsers 
   + jaxp
   + xml文件解析工厂
     - dom
     - sax
     - XSLT
     - stax
 * javax.xml.validation
   + JAXP 验证框架
   + 它定义了用于应用程序缓冲模式（比如 W3C Schema）和验证 XML 文档的 API。
   
 * javax.xml.datatype
   + 它定义了新的 Java 类型，并完成了 W3C Schema 数据类型与 Java 类型之间的映射。
 * javax.xml.soap 
   + soap支持，应该是jax-rpc的使用，已经使用java-ws代替。
 * javax.xml.transform 
   + XSLT
   + 允许将XML文档转换为数据其他形式
   
 * javax.xml.ws 
   + jax-ws支持
 * javax.xml.stream 
   + StAX (Streaming API for XML)
   + 是一种针对XML的流式拉分析API
   + 流模型它有两种变体--“推”模型和“拉”模型。 
      - 推模型：就是我们常说的SAX，它是一种靠事件驱动的模型。当它每发现一个节点就引发一个事件，而我们需要编写这些事件的处理程序。这样的做法很麻烦，且不灵活。
      - 拉模型：在遍历文档时，会把感兴趣的部分从读取器中拉出，不需要引发事件，允许我们选择性地处理节点。这大大提高了灵活性，以及整体效率。
   
 * 在程序中访问和操作XML文件一般有两种模型：DOM（文档对象模型）和流模型。
   + DOM优点：允许编辑和更新XML文档，可以随机访问文档中的数据，可以使用XPath（XML Path Language，是一种从XML文档中搜索节点的查询语言）查询。 
   + DOM缺点：需要一次性加载整个文档到内存中，对于大型文档，会造成性能问题。
   + 流模型优点：对XML文件的访问采用流的概念，在任何时候内存中只有当前节点，解决了DOM的性能问题。 
   + 流模型缺点：是只读的，并且只能向前，不能在文档中执行向后导航操作。
   
### org.ietf.jgss 
 * java安全接口
 
### org.omg
 * 不怎么用
 
### org.w3c.dom
 * dom 对象树
 
### org.xml.sax 
 * sax解析xml
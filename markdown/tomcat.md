## tomcat

### web应用目录
 ```
  bmn----web应用根目录
    |----htlm, jsp, js, css, image 根目录下的文件外界可以直接访问
    |----WEB-INF  这个目录下的文件外界无法访问，由web应用服务器负责调用
      |----classes (java类)目录
      |----lib (java运行时所需要的jar)
      |----web.xml (web应用配置文件)
     
```

### tomcat 组件结构
 * tomcat由组件组成，其中核心组件是Servlet容器组件，它是所有其它组件的顶层容器
 * 每个组件都可以在tomcat/conf/server.xml中进行配置
 ``` 
    <Server> : 代表整个Servlet容器组件，是最顶层元素，可以包含一个或多个Service元素
        <Service> : 包含一个Engine元素，多个Connector元素，所有Connector共享一个Engine
            <Connector/>
            <Engine> : 它处理在同一个<Service>中所有<Connector>接收到的客户端请求
                <Host> : 在一个Engine中可台包含多个Host， 它代表一个虚拟主机（即一个服务器程序可以部署在多个不同IP的服务器主机上）
                    <Context/> ： 代表虚拟主机上单个web应用，可以是多个
                </Host>
            </Engine>
        </Service>
    </Server>
 ```
 
### 虚拟目录的映射方式
 * tomcat6后，支持自动映射即tomcat服务器会自动管理webapps目录下的所有web应用，并把它映射成虚拟目录。不需要配置Context
 * web应用不放在webapps目录，需要配置Context
 
### Context元素
 * tomcat加载一个web应用，查找顺序
   + 到tomcat安装目录/conf/Context.xml文件中查找元素
   + 到tomcat安装目录/conf/[enginename]/[hostname]/context.xml.default文件中查找
   + 到tomcat安装目录/conf/[enginename]/[hostname]/[contextpath].xml文件中查找
   + 到web应用的META-INF/context.xml文件中查找
   + 到tomcat安装目录/conf/server.xml文件中查找，只适用于单个web应用
   
### 将项目部署为Tomcat默认应用
 * tomcat默认主目录是webapps/root
 * 方法一
   + 在conf/server.xml添加context
   `<Contextpath=""docBase="C:\tomcat7\webapps\myapp"reloadable="true"debug="0">Context>`
 * 方法二
   + 删除root目录下内容，换成自己项目内容
 * 方法三
   + tomcat5.0以下会在conf/Catalina/localhost目录下生成一个ROOT.xml；5.0则不生成
   + 通过添加ROOT.xml
   ``` 
    Xml version='1.0' encoding='utf-8'?>
    <ContextcrossContext="true"docBase="C:\tomcat7\webapps\myapp"path=""reloadable="true">
    Context>
   ```
### Context元素属性
 * path: 指定访问该web应用的URL入口
 * docBase: 指定web应用的文件路径，可以写绝对，也可以写相对appBase(<Host>属性)属性的相对路径
 * className: 指定实现Context组件的java类名，这个类必须实现org.apache.catalina.Context接口
 默认是org.apache.catalina.core.StandardContext
 * reloadable: 如果是ture, tomcat服务器会在运行状态下监控WEB-INF/classes和lib目录的class文件的改动，
 以及web.xml改动。如果有更新服务器会自动更新加载web应用
 
### web.xml
 * 文件必须放在WEB-INF下，7.0以后可以不使用web.xml, 而是使用注解
 
 
### Connector
 * 主要功能是接收连接请求，创建request,response对象用于和请求端交换数；
 然后分配线程让Engine来处理这个请求
 * 主要分为HttpConnector, AjpConnector
#### protocal
 * BIO, NIO, NIO2, APR
 * 如果没有指定protocal， 则使用默认Http/1.1
   + 作用：在tomcat7以下，自动选取使用BIO或APR(如果找到APR需要的本地库，则使用APR, 否则使用BIO);
   在tomcat8中，自动选取NIO和APR
   
 * acceptor队列
   + 每个socket连接成功后放入acceptor队列中。等待处理
   
 * BIO实现
   + Acceptor从acceptor队列获取socket连接，然后找到Worker线程池中找出空闲线程处理socket，如果没有空闲线程则Acceptor阻塞
   + 因为http/1.1使用长连接，请求结束后socket没有关闭，所以线程一直等待释放，导致线程一直被占用
   + tomcat可以同时处理的socket数目不超过线程池大小
 * BIO
   + Acceptor从acceptor队列获取socket连接，把scoket放到阻塞队列，然后poller取出socket注册到selector上，找出可读的socket
   并使用worket线程池中空闲线程去处理
#### 参数
 * acceptCount
   + accept队列长度，默认100， 当队列满时，请求一律被拒绝
 * maxConnections
   + 接收与处理的最大连接数，tomcat维持的最大连接数
   + 当边到maxConnections后，Acceptor线程不会读取acceptor队列中的连接；这时acceptor队列中的线程会一直阻塞，
   直到连接数小于maxConnections。如果设置为-1，则连接数不受限制
   + 默认值：与连接器有关：NIO默认是10000， APR默认是8192， BIO默认是maxThreads大小
   + maxConnections与tomcat运行模式有关
 * maxThreads
   + 请求处理线程最大数量（worker线程数）。默认200。如果使用自定义Executor，则使用自定义大小
   + 与cpu核数有关
 * 服务器可以同时接收连接数为maxConnections + acceptCount
   + 如果acceptCount很长，后面进入的请求等待时间会很长
   + 如果acceptCount很短，后面进入的请求立马返回connection refused;
   
#### 线程池
 * 属性
   + name
   + maxThreads：最大线程数
   + minSpareThreads：最小线程数
   + maxIdleTime: 线程空闲存活时间，单位ms, 默认60000
   + daemon: 默认true
   + threadPriority
   + namePrefix
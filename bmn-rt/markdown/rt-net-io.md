## java网络编程
### 安全性
 * 从网络加载的代码通常禁止哪些操作
   + 不能访问任意内存地址，这个限制是java语言本身和字节码验证器的特性。
   + 不能访问本地文件系统
   + 不能打印文档
   + 不能读取与写入系统剪贴板
   + 不能启动客户端的其它程序。不能使用System.exec()和Runtime.exec()
   + 不能加载内置库或定义内置方法调用。
   + 不能允许使用System.getProperty()获取用户或用户机器信息
   + 不可以定义任何系统属性
   + 不可以创建或操作任何不在同一个ThreadGroup中的Thread
   + 不可以定义或使用ClassLoader, SecurityManager, ContentHandlerFactory, SocketImplFactory, URLStreamHandlerFactory的新实例，必须使用已经存在实例。
   
### URI
 * 格式
   + scheme:scheme-specific-part
   + scheme-specific-part没有特定语法，但是很多具有结构层次的形式
   `//authority/path?query`
   
### URN
 * urn:namcespace:resource_name
 * namespace: 某个授权机构维护的某类资源的集合
   + IANA负责将命名空间分发给不同的组织
   
### URL
 * protocal://username@host:port/path/filename?query#fragment
 * protocal是URI模式的另一种叫法（模式是URI RFC中的说法，协议是java文档中的说法）
 * path是指定服务器的某个目录，路径是相对于服务器文档根目录的，而不必是服务器上文件系统的根目录。
 * filename指目录中的文件，可以省略。
   + 如果省略则由服务器判断发送哪个文件，如该目录下的index.html, 
   + 如果省略则可能服务器返回403错误
 * fragment
   + 如果远程资源是html, 那么此片段标识符将指定该html文档中的一个锚(anchor)
   + 如果远程资源是xml, 指一个XPoint
   + 有些文档称为段(section), java文档称为"Ref"
   
### 相对URL
 * 如果相对链接以"/"开头，那么它是相对于文档根目录的，而不是当前文件所在目录
 * 如果相对链接不是"/"开始，那么它是相对于当前文件所在目录
 
### html
 * 超文本标记语言
 * 属性值可以引在双引号或单引号内
   + 引号只有在值包含内嵌空格时才是必需的。在处理html时，有和没有引号的属性值都要求能够处理。
### XML
 * 可扩展标记语言
 * 由于html标签语言是固定的，
 * 由于xml自定义的所以通过stylesheet来描述每一项应当如何显示。
 * 可能DTD(文档类型定义)来限制xml文档内容
 * 格式严格，标签必须有结束标签，属性必须用引号

### xhtml
 * 遵循xml严格格式规则的html
 
### 响应码
 * 200-299
   + 表示接收、理解并接受了请求
 * 300-399
   + 表示浏览器需要跳转到不同的页面
 * 400-499
   + 表示客户端某些地方有错误。
 * 500-599
   + 表示服务器错误

### MIME 媒体类型
 * 多媒体网络邮件扩展
 * 类型/子类型
 * 扩展子类型
   + 使用前缀x-
   
## 流
### 输出流 OutputStream
 * 基本方法write(int b); 
   + 这个方法接受一个0-255之间的整数使用参数，
   + 使用int作为参数，实际是写入一个无符号字节，java没有无符号字节数据类型，所以使用int来代替只写入低8位，其它3字节被忽略

### 输入流 InputStream
 * 基本方法int read();
 * 所有3个read方法都返回-1表示流结束。
   + 如果流结束，而尚有没有读取的数据，多字节方法会返回这些数据，直到缓冲区清空，其它任何一个read调用会返回-1。
 
### PrintStream
 * 5个标准OutputStream方法的声明没有抛出异常
 * 不能设置字符编码
 * println()的输出是与平台相关的
   + 对称写入控制台时不会产生问题
   + 但对于遵循准确协议的网络客户端与服务器而言
   
### 避免使用readline
 * 有一个隐含的bug, 它们不一定会把一个回车看作行的结束，
 * readline()只识别换行符或回车+换行符对。
   + 如果在流中检测到回车时，readline会继续等待查看一下个字符是否为换行。
   + 如果是换行则丢掉回车与换行，返回string
   + 如果不是换行，就丢掉回车，返回string, 这个额外的字符会作为下一行的一部分读取。
   + 但是如果回车是流的最后一个字符，那么readline就会挂起，等待最后一个字符出现，但这个字符永远不会出现。
 * 这个问题在读取文件时不太明显，因为有-1表示流结束
 * 但是在网络中在最后一个字符这后停止发送数据，则可能是超时。如果不够幸运，程序将永远挂起。
 
## internet地址

### InetAddress
 * InetAddress类是java对IP地址（包括IPv4, IPv6）的高级表示。
 * 创建实例
   + getByName: 通过DNS查询, 同时会缓存查询结果为下次查询快速返回使用
   + getAllByName: 与getByName一样，同时会返回一个域名下的所有ip
   + getByAddress: 不进行DNS查询，通过ip
 * get会通过DNS查询
 * getHostName()获取主机名, 如果没有主机名返回ip
 * getHostAddress()返回ip
 * getAddress() 返回是Ip字符数组，一般用来判断是ip4，ip6。通过length == 4， length== 16
 
### 地址类型
 * isAnyLocalAddress()
   + 是否为通配地址，
   + IPv4 0.0.0.0
   + IPv6 0.0.0.0.0.0.0.0
   + 通配地址可以匹配本地系统中的所有地址
 * isLookbackAddress()
   + 是否为回路地址
   + IPv4 127.0.0.1
   + IPv6 0.0.0.0.0.0.0.1(又写作::1)
 * isLinkLocalAddress()
   + 在地址是IPv6本地链接地址返回true
   + 路由器不会把这些包转发出本地子网
   + 所有本地链接地址以8个字节FE80:0000:0000:0000开头。后8字节用本地地址填充，通常复制为由以太网卡生产商分配的以太网MAC地址。
 * isSiteLocalAddress()
   + 在地址是IPv6本地网站地址时返回true
   + 会被路由器在网站或校园内转发，但不应当转发出网站。
   + 本地网站地址以8个字节FEC0:0000:0000:0000开头。后8字节用本地地址填充，通常复制为由以太网卡生产商分配的以太网MAC地址。
 * isMulticastAddress()
   + 在地址是组播地址时返回true
   + 组播会将内容广播给所有预定的计算机，而不是某一台。
   + IPv4 组播地址位于224.0.0.0-239.255.255.255
   + IPv6 组播地址都以字节FF开头
 * isMCGlobal()
   + 在地址是全球组播地址时返回true
   + 全球组播地址是世界范围内的预定地址。
   + 所有组播地址都在FF开头。
   + IPv6 全球组播地址以FF0E或FF1E开头，这取决于此组播地址是已知的永久分配地址，还是临时地址。
   + IPv4 所有组播地址都是全球范围的。
 * isMCOrgLocal()
   + 在地址是组织范围组播地址时返回true
   + 可能包括公司或组织所有网站的预定地址，但不包括组织外部地址。
   + 组织组播地址以FF08或FF18开头，取决组播地址是永久分配地址，还是临时地址。
 * isMCSiteLocal()
   + 在地址是网站范围组播地址时返回true
   + 寻址到风站范围地址的包只会在本地网站内传输。
   + 网站组播地址以FF05或FF15开头，取决组播地址是永久分配地址，还是临时地址。
 * isMCLinkLocal()
   + 在地址是子网范围组播地址时返回true
   + 寻址到本地链接地址的包只会在自己的子网内传输
   + 本地铰接组播地址以FF02或FF12开头，取决组播地址是永久分配地址，还是临时地址。
 * isMCNodeLocal()
   + 在地址是本地接口组播地址时返回true
   + 寻址到本地接口地址的包不能发送出最初的网络接口，即使是相同节点的不同网络接口也不行。
   + 主要用于网络调试和测试。
   + 本地接口组播地址以两个字节FF01或FF11开头，取决组播地址是永久分配地址，还是临时地址。
   
### NetworkInterface
 * 用来表示一个本地IP地址。
 * 表示物理硬件或虚拟地址
 
### URL
 * 判断url支持的Protocal
 * 创建相对url通过new URL(URL url, String relative);
 * 指定一个URLStreamHandler, 协议处理器
 
#### 分解URL
 * 结构
   + 模式，java中叫协议
   + 授权机构
   + 路径
   + 片段
   + 查询字符串
 * 授权机构
   + 用户信息
   + 主机
   + 端口
 * getFile()
   + java不将URL分为单独的路径和文件部分。
   + 从主机名后的第一个斜线一直到开始片段标识符的#号之前的字符，都被认为是文件的部分。
 * getPath()
   + 与getFile()基本相同，只是不包括查询字符串
 * getUserInfo()
   + 位于模式后，主机前面，@符号将它们分隔开
#### 从URL获取数据
 * openStream()
   + 执行客户端与服务器端必要的握手，返回一个可以读取数据的InputStream。
   + 从这个InputStream获得的数据是URL所指向文件的原始内容
   + 它不包括任何Http首部或任何与协议有关的信息
 * openConnection()
   + 通过URLConnection可以让你访问服务器发送的所有数据
   + 包括原始数据与此协议指定的所有元数据
   + 还可以写回数据
 * openConnection(Proxy proxy)
   + 这个会覆盖通过设置的socksProxyHost, socksProxyPort, http.proxyHost, http.proxyPort, http.nonProxyHosts
   + 和类似系统属性进行设置的任何代理服务器
   + 如果协议处理器不支持代理，此参数将被忽略，如果可能的话就直接进行连接。
 * getContent
   + 获取URL指向的数据，尝试将其转换为某种对象
   + 如果：文本转为InputStream, 图片转为ImageProducer
   + 它是通过从服务器获取数据的MIME首部中的Content-type获取，如果没有Content-type或不认识的则返回InputStream
 * getContent(Class[] class) 
   + 判断是否为指定的class对象
 
#### 工具方法
 * sameFile(URL other)
   + 判断两个url是否指向相同的文件
 * URLEncoder, URLDecoder
   + 对URL进行编码
   
### URI
 * 表示资源定义，不依赖于底层协议处理器。
 * 编码
   + 可以是ASCII, 也可以是Unicode
 * 有模式的URI是绝对URI, 没有模式的URI是相对URI, 
 * isOpaque()
   + 当为层次URI时，返回false
   + 当为非层次URI时，返回true
   + 如果URI不透明，所能得到的是模式，模式特有部分，片段标识符。
   + 如果URI是层次，那还能获取层次信息。如：getHost, getPath()
 
 * 解析相对URI
   
### 代理
 * 系统属性
   + 设置系统属性，指出本地代理服务器的地址
   + 可以通过启动参数，或System.setProperty()
   + http.proxyHost, http.proxyPort, http.nonProxyHosts(不被代理的主机，可以使用通配符，多个用“|”分隔)
   + ftp.proxyHost, ftp.proxyPort, ftp.nonProxyHosts
   + socksProxyHost, socksProxyPort
#### Proxy 类
 * 三种代理
   + Http
   + Socket
   + 直接连接（没有代理）
   
### Socket
 * isClosed()
   + 当打开的socket关闭后，返回true
   + 否则返回false, 如果没有进行连接的socket也返回false
 * isConnected()
   + 表示socket是否曾经连接过远程主机
 * 服务类型
   + 低成本
   + 高可靠性
   + 最高吞吐量
   + 最小延迟
   + 通过setTrafficClass
   

### UDP
 * java中UDP实现分为两个类：DatagramPacket和DatagramScoket
 * DatagramPacket
   + 将数据字节填充到称为数据报的UDP包中，让你来解包接收的数据报。
 * DatagramSocket
   + 可以收发UDP数据报
   
   
### RMI
 * java.rmi
   + 定义了客户端所见的类、接口和异常，当编写要访问远程对象但本身不是远程对象的程序时使用。
 * java.rmi.server
   + 定义了服务器端可见的类、接口和异常，当编写被客户端调用的远程对象时使用。
 * java.rmi.registry
   + 定义了用于查找和命名远程对象的类、接口、异常
 
#### 服务器端
 * 远程对象可以高性能计算机运行，为低性能客户端计算结果
 * 创建远程对象，
   + 首先要定义一个扩展了java.rmi.Remote接口的接口
   + 子接口每个方法都必须声明抛出RemoteException
 * 定义远程类
   + 实现UnicastRemoteObject或通过UnicastRemoteObject.exportObject()
 * 通过Naming bind
   
#### 编译stub
 * stub作为本地对象与运行于服务器的远程对象之间的中间人
 * 服务器的各个远程对象都以客户端上的stub类表示
 * 对象通过继承实现UnicastRemoteObject的类，可以通过rmic自动生成stub
 * 通过UnicastRemoteObject.exportObject(), 需要手动生成stub
 * 启动注册表服务器
   + 实际上是两个服务器，一个是远程对象本身，一个是允许本地客户端下载远程对象引用的注册表
   + 通过rmiregistry port &
   + 然后启动 java xxxServer(由rmic生成的)
   
#### 客户端
 * 通过Naming.lookup("rmi://host:port/name")
 * name是注册的远程对象名
 * 运行客户端
   + jdk1.5前需要由rmic生成stub类，放到客户端路径下。1dk1.5之后不需要这个类了。
   + 需要实现了Remote接口的子接口
 * 在运行时加载类
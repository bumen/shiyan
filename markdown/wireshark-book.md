## wireshark书

### 网络硬件
 * 集线器
   + 工作在物理层：只负责传输
   + 工作原理：
   如：集线器有4个口，1,2,3,4。分别4台机器a,b,c,d。
   如果a机器连接1口，向d机器连接4口发送消息。此时集线器把消息发给所有端口上的机器，由上层协议去判断是否是发给本机的消息。
   + 半双工通信：同一时间只有一有个设置在发送消息，如果多台同时发送，则会发生消息碰撞，造成消息丢失。然后选择消息的机器会重发消息一般需要重发3-4次，大降低了网络性能
   所以集线器被淘汰
   
 * 交换机
   + 工作在数据链路层：解析mac地址，不同机器之通信
   + 工作原理：把消息发给指定机器，而不是所有机器。所以需要工作在数据链路层，去解析消息中的mac地址。然后根据mac地址确定要发送到对应的机器
   + 可以实现全双工通信
 * 路由器
   + 工作在网络层：解析ip地址，不同网络中的机器这间通信
   + 工作原理：由于需要实现不同网络中机器通信，所以需要ip地址判断网络。以所是工作在网络层
   
### 流量分类
 * 广播
   + 两种：
   一种是数据链路层广播：mac地址为FF:FF:FF:FF:FF:FF
   一种是网络层广播: ip地址为网络地址最大值.192.168.0.xxx范围的网络，子网掩码是：255.255.255.0。则广播地址为192.168.0.255
 * 多播
 * 单播
   
### 监听网络线路

#### 网卡混杂模式与非混杂模式
 * 由于一台机器会接收到不是发给自己的数据包，为了减少cpu消耗网卡会丢掉这些无用的包。
 如果是混杂模式，则不是丢掉。所以如果想监听线路上传输的每一个包则需要把网卡配置为混杂模式。
 
#### 在集线器上监听
 * 把监听设置连接到集线器上任意空闲商品即可获取所有通过该集线发送和接收的消息。
 
#### 在交换机上监听
 * 
 
### view菜单
 
#### 时间格式化
 * 如果时间配置为相对时间
   + Seconds Since Beginning of Capture： 相对于捕获第一个包开始
   同时这个包配置为Set Time Reference，则之后的数据包都是第一个包的相对时间。用于查看两包之间的时间差很方便
   
#### Coloring Rules
 * 颜色配置

### edit菜单

#### package标记
 * 可以只保存标记过和数据包
 
### Capture菜单
 * Options选项, 分几块
   + Capture Interface ： 配置要捕获的接口，过滤器。是否开户混合模式
   + Output File：捕获文件，对于捕获的包直接存入文件，对文件的一些配置
   + Display Options：捕获显示：当捕获一定数据的包时会有相当的负担。最好不开启
   + Name Resolution：数据链路层，网络层，传输层解析
   + Stop Capture：配置停止捕获条件
   

### 过滤器
 * 捕获过滤器：只有满足给定条件的包才捕获
   + 只捕获需要的包，可以快速分析，降低捕获资源消耗
   
 * 显示过滤器：在已经捕获的集合里，显示满足给定条件的包，隐藏不满足条件包
   
### BPF语法(Berkeley Packet Filter)
 * 大多数嗅探器都支持这种语法，因为都依赖使用BPF的libpcap/WinPcap
 * 使用BPF语法创建的过滤器被称为表达式
 * 每个表达式包括一个或多个原语，每个原语包含一个或多个限定词，然后跟着一个ID名字或者数字
 * BPF限定词
   + Type：指出名字或数据所代表的意义。 host, net, port。
   + Dir: 指明传输方向是前往还是来自名字或数字。src, dest
   + Proto: 限定所要匹配的协议。 ether, ip, tcp, udp, http, ftp
 * 原语
   + dest host 172.168.0.1 && tcp port 80
   + 解释：dest host ID 是一个原语（由两个限定词组成）， &&是操作符， tcp port 80 是一个原语（如两个限定词组成）
   + 如果原语中没有指定Type限定词，则默认使用host
 * 逻辑运算符
   + &&，||，!
   + and, or, xor, not
   
 * 比较操作符
   + ==
   + !=
   + >
   + <
   + >=
   + <=
   
### 快捷键
 * Ctrl + Shift + E: 打开协议解析器配置
   
### 协议域过滤器
 * 我们可以通过检查协议头中的每个字节来创建基于那些数据的特殊过滤器
   + icmp[0] 反回0偏移一个字节的整型值
   + imcp[0:2] 返回0偏移两个字节的整型值，可以与十六进制比较。如： icmp[0:2] == 0x0301
 
### Statistics

 
#### Endpoint端点
 * 指网络上能够发送或接收数据的一台设备
 * 查看指定端点上的流量
 
#### Converstations会话
 * 查看会话之间流量
 
#### Endpoint与Converstations综合分析
 * 先通过Endpoint查看进行数据量大少排序，查看哪个地址数据量最大，从而找到相应的ip。一般第一个最大的是本地地址
 * 再通过Converstations 查看两个端点通信的数据量，是什么
   
#### Protocal Hierarchy 协议分层统计
 * 如果想知道捕获的数据中，不同协议占用百分比
 
#### Packet Lengths数据包大小分析
 * 

#### IO Graphs
 * 查看数据峰值 
 
#### round trip time 双向时间图
 * 双向时间：你的数据包抵达目的地和这个数据包低达所发送的确认返回到你的时间之和
 * 用于分析慢点或瓶颈，以确定是否存在延迟
 
#### Flow Graph 数据流图
 * 将主机连接显示出来，并将流量组织到一起
 
### 名字解析
 * 通过Capture options中设置
 * Mac地址解析：
   + 使用arp协议试图将数据链路层MAC地址转换到网络层地址。如果转换失败，将使用程序目录中的ethers文件尝试进行转换。
   + 最后方案是将MAC地址前3个字节转换到设备的IEEE指定制造商名称
 * 网络名称解析：
   + 试图将网络层地址转换到一个易读的DNS名称
 * 传输名称解析：
   + 试图将一个端口号转换成一个与其相关的名字。如80转为Http
 * 弊端
   + 名称解析可能会失败
   + 名称解析在每次打开一个捕获文件的时候都要重新进行一次，因为就不会将解析名称保存在文件中。如果名称解析所使用的服务器不可用，则解析失败
   + 对DNS名称解析的依赖，会产生额外的数据包，也就是说你的捕获文件中可能会被解析那些基于DNS地址的流量所占据
   + 名称解析会带来一个额外的处理开销
  
### 协议解析
 * 通过协议解析器，将原始数据流解析为不同协议的数据包格式显示出来
 
#### 自定义协议解析器
 * 默认wireshark会自动为每个一个原始流使用默认解析器，通过默认端口配置不同协议解析。
   + 如：443端口默认为ssl协议，如果有发送到443端口的数据，则默认使用ssl协议解析。如果443端口用于其它应用，则会解析不正确，此时需要重新配置443端口解析器
 * 强制更换解析器，不会保存在文件中，下次打开捕获文件还需要重新设置
 
### 跟踪TCP流
 * 
 
### Analyze -> Expert Information分析
 * 专家分析
   + Chat: 对话，关于通信的基本信息
   + Note: 注意，正常通信中的异常信息
   + Warnning: 警告，不是正常通信中的异常数据包
   + Error: 错误，数据我中错误，或者解析器解析时错误
  
 * Chat
   + 窗口更新 tcp windows update：由接收者发送，用来通知发送者tcp接收窗口的大小已经改变
 
 * Note
   + TCP重传，当一台主机没有收到下一个期望序列号的数据时，它会生成最近收到一次数据的重复ACK
   + 零窗口探查，在一个零窗口包被发送之后，用来监视TCP接收窗口的状态
   + 保活ACK, 用来响应保活数据包
   + 零窗口探查ACK, 用来响应零窗口探查数据包
   + 窗口已满，用来通知传输主要其接收者的TCP接收窗口已满
 * Warnning
   + 上一段丢失，指明数据包丢失。发生在当数据流中一个期望的序列号被跳过时
   + 收到丢失数据包的ACK, 发生在当一个数据包已经确认丢失但收到了其ACK数据包时
   + 保活，当一个连接的保活数据包出现时触发
   + 零窗口，当接收方已经达到TCP接收窗口大小时，发出一个零窗口通知，要求发送方停止传输数据
   + 乱序，当数据包被乱序接收时，会利用序列号进行检测
   + 快速重传输，一次重传会在收到一个重复ACK的20毫秒内进行
   
### 协议
 * ARP（Address Resolution Protocol）
   + 是TCP/IP网络中用来将IP地址解析为MAC地址的过程称为地址解析协议
   + 分为ARP请求与ARP响应
   + ARP请求：带着本机的IP与MAC地址，目标IP。通过广播方式查询目标IP对应MAC
   请求中的以太网头目的地址是ff:ff:ff:ff:ff:ff， 这是一个以太网的广播地址，所有发送到这个地址的数据包都会被广播到不前网段中的所有设备
   ``` 
    Frame 20: 60 bytes on wire (480 bits), 60 bytes captured (480 bits) on interface 0
    Ethernet II, Src: AsixElec_aa:33:15 (00:0e:c6:aa:33:15), Dst: Broadcast (ff:ff:ff:ff:ff:ff)
        Destination: Broadcast (ff:ff:ff:ff:ff:ff)
        Source: AsixElec_aa:33:15 (00:0e:c6:aa:33:15)
        Type: ARP (0x0806)
        Padding: 000000000000000000000000000000000000
    Address Resolution Protocol (request)
        Hardware type: Ethernet (1)
        Protocol type: IPv4 (0x0800)
        Hardware size: 6
        Protocol size: 4
        Opcode: request (1)
        Sender MAC address: AsixElec_aa:33:15 (00:0e:c6:aa:33:15)
        Sender IP address: 172.16.22.210
        Target MAC address: 00:00:00_00:00:00 (00:00:00:00:00:00)
        Target IP address: 172.16.22.64
   ```
   + ARP响应：带着本机的IP与MAC地址，请求方的IP与MAC
   
   + 无偿ARP:gratuitous: 与ARP请求很像。当一个设备的ip改变时会发送，可一些操作系统启动时会发送
   与ARP请求区别是：Sender IP address 与 Target IP address 是相同的ip
   ``` 
    Frame 613: 60 bytes on wire (480 bits), 60 bytes captured (480 bits) on interface 0
    Ethernet II, Src: HuaweiTe_77:79:48 (c4:b8:b4:77:79:48), Dst: Broadcast (ff:ff:ff:ff:ff:ff)
        Destination: Broadcast (ff:ff:ff:ff:ff:ff)
        Source: HuaweiTe_77:79:48 (c4:b8:b4:77:79:48)
        Type: ARP (0x0806)
        Padding: 000000000000000000000000000000000000
    Address Resolution Protocol (request/gratuitous ARP)
        Hardware type: Ethernet (1)
        Protocol type: IPv4 (0x0800)
        Hardware size: 6
        Protocol size: 4
        Opcode: request (1)
        [Is gratuitous: True]
        Sender MAC address: HuaweiTe_77:79:48 (c4:b8:b4:77:79:48)
        Sender IP address: 172.16.22.1
        Target MAC address: 00:00:00_00:00:00 (00:00:00:00:00:00)
        Target IP address: 172.16.22.1
   ```
   
 * IP协议
   + 网络部分+主机部分
   + 网络部分用来标识设备所连接到的局域网
   + 主机部分用来标识这个网络中的设备本身
   + IP地址和网络掩码的简便表示：CIDR(Classless Inter-Domain Routing)
   192.168.1.1与255.255.0.0表示为192.168.1.1/16。16表示IP地址中网络部分位数的数字
   
 * IP分片
   + 一个数据包分片主要基于数据链路层协议所使用的最大传输单元MTU(Maximum Transmission Unit),以及使用第2层协议的设备配置情况。
   大多数第2层数据链路协议使用的以太网，以太网的MTU默认是1500，也就是说以太网上所能传输的最大数据包大小是1500字节（并不包括14字节的以太网头本身）
     - MTU可以手工设定，其可以在Windows或者Linux系统上修改或者在管理路由器的界面上修改
   + 当传输一个IP数据包时，如果数据包大小MTU，则需要分片，分片步骤
     - 设备将数据分为若干个可成功进行传输的数据包
     - 每个IP头的总长度域会被设置为每个分片的片段长度
     - 更多分片标志将会在数据流的所有数据包中设置为1，除了最后一个数据包。
     - IP头中分片部分的分片偏移将会被设置
     - 数据包被发送出去
     
 * TCP协议
   + RST标志：用来指出连接被异常中止或拒绝连接请求
   
 * ICMP协议
   + 负责提供在TCP/IP网络上设备，服务以及路由器可用性信息
   
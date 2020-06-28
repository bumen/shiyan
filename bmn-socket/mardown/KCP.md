### KCP协议
 * TCP
   + 是为流量设计的，每秒内可以传输多少KB的数量
   + TCP是一个条流速很慢，但每秒流量很大的运河
 * KCP是为流速设计的，单个数据包从一端发送到一端需要多少时间。
   + KCP是水流湍急的小激流
   
 * 传输层协议，保证可靠，同时保证传输速度
 
### 名词
 * ISP: Internet Service Provider, 互联网服务提供商
 * Qos: Quality of Service, 服务质量
 * FEC: Forward error correction, 前向纠错
   + 空间换时间
   + 优点
     - 数据包只需要传输一次
     - 传输延迟不会受到 RTT(Round Trip Time) 的影响
     - 不会增加额外的延迟时间
   + 缺点
     - 需要增加冗余数据包
     - 降低了传输信道的利用率
     
 * ARQ: Automatic Repeat-reQuest, 后向纠错
   + 效率低
     - 需要发送发缓存发送消息，从而影响传输速度
     - 需要ACK，从而影响传输效率
     
 * ECC: Error correction code, 纠错码
 * RTO: Retransmission Timeout, 重传超时时间
 * RTT：Round Trip Time：发送一个数据包到收到对应的ACK，所花费的时间
 * MTU：
 * cwnd：congestion windows, 拥塞窗口
 * rwnd：receiver window, 接收方窗口大小
 * snd_queue
 * snd_nxt
 * snd_una
 
### 可靠性保证 Reliable
 * ARQ模型
   + Automatic Repeat-reQuest
   
   
### EAGAIN
 * 意思是再重试一下
 * 在非阻塞环境下，读取数据时如果没有可用数据时，不会阻塞线程而是返回一个EAGAIN错误
 * KCP，在用户read时没有可用数据时，返回一个-1表示EAGAIN
 
 
### conv 作用
 * 连接迁移
 ``` 
    很简单，任何一条kcp连接不再以 IP 及端口四元组标识，而是以一个 32 位的随机数作为 ID 来标识，
    这样就算 IP 或者端口发生变化时，只要 ID 不变，这条连接依然维持着，上层业务逻辑感知不到变化，不会中断，也就不需要重连。
    由于这个 ID 是客户端随机产生的，并且长度有 32 位，所以冲突概率非常低
 ```
 
### 作用
 * 相同的原始数据包不要两次input到KCP，否则将会导致 kcp以为对方重发了，这样会产生更多的ack占用额外带宽。
 * 字节顺序小端：因为今天大部分设备和处理器都是小端的，我这么写就是让90%的设备运行更快点，调试更方便点。
   - 低字节在前
 * kcp 的connect close状态监听
   - 使用tcp连接成功后，再使用kcp替换
   - 使用kcptun. 自己封装了一层syn fin包
   - 加心跳是处理连接成功后可以用来监听close
   
 * udp EAGAIN
 ``` 
    UDP的 EAGAIN和 TCP的 EAGAIN 不是一回事情，你用 TCP，只要远端不主动接受数据，接收缓存满了，或者中间网络慢了，
    都会发生 EAGAIN。UDP不一样，不管远端接不接受，
    中间网络情形如何，只要数据顺利从本机网卡出去，就不会 EAGAIN，UDP唯一 EAGAIN的时候是：
    应用程序对 sendto的调用大于网卡实际发送能力，发送缓存倍填满了，就 EAGAIN了。
 ```
 
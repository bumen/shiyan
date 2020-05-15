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
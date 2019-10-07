## netty重点
 * channel
 * pipeline
 * inbound, outbound
 * eventgroup
 * schedule
   + DefaultPriorityQueue 实现。排序后有索引
 * HashWheelTimer
 * ChannelOutboundBuffer
 * Recycler
 
### netty pipeline
 * netty outbound事件，inbound事件都是在channel io线程处理，所以各事件不会冲突
   + outbound最终是head 
   + inbound最终是tail
   + 所以读写不能同时进行
 
### IdleStateHandler
 * 读写超时判断
 * 自定义超时时间
 * 通过添加定时任务实现。每次定时任务达到判断时间是否有写或读成功
 * 如果超时，则发出超时事件
 * 自己可以写心跳检查超时后，可以发心跳包，多少次没有心跳后则超时
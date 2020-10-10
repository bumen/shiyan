### 线程模型

#### 传统阻塞 I/O 服务模型
 * 每一个消息处理就分配一个任务去执行
 * 缺点：当并发数较大时，需要创建大量线程来处理连接，系统资源占用较大
 
 
#### Reactor 模式
 * 单 Reactor 单线程
   + 接待员和侍应生是同一个人，全程为顾客服务
 * 单 Reactor 多线程
   + 1 个接待员，多个侍应生，接待员只负责接待
 * 主从 Reactor 多线程
   + 多个接待员，多个侍应生



#### Proactor 模型
 * 异步回调
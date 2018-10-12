## TCP相关

### 单机最大TCP连接数

### 构造虚拟TCP请求连接客户端

### 构造虚拟TCP响应连接服务器

### linux文件句柄
 * 1G内存的机器上大约有10万个句柄左右
 
### 检测TCP连接是否有效

#### 轮询扫描法
 * Map<uid, last_packet_time>，当请求来时，实时更新map
 * 启动一个线程，轮询扫描这个map
 * 效率低

#### 多timer触发法
 * Map<uid, last_packet_time>,记录uid最近请求时间
 * 当请求来时，实时更新map, 同时对这个uid请求包启动一个timer，30s后触发
 * 30s后与map里时间，判断是否超时
 * 比较耗资源
 
#### 环形队列法
 * 用数组，组成的环
 * 30s就是 array[30] 大小的环。 每个位置是一个set队列
 * 启动一个timer，每一秒扫描一次，curIndex移动一次。
 * 使用一个map<uid, slot> 记录uid上次放的哪个位置
 * 当请求来时，请求上次slot. 从队列中删除uid
 * 将uid加入到curIndex所在的位置 
 
### 高效延时消息
 * 也使用环形队列法
 * 如果48小时后触发
   + 创建环形队列3600大小
   + 任务集合，Set<Task>
   + 一个timer每一个触发一次
   + Task需要两个参数
      - Cycle-num : 环形队列要走几圈，才会触发。因为一圈是1小时
      - function: 触发的任务
 * 问题
   + 如果关服，怎么处理
      - 创建任务时持久化到磁盘
   + Set多在合适
      - 可以调整, Cycle-num 与环形队列大小
 
### 状态转换
 * 主动连接方
   + SYN_SENT
   + ESTABLISHED， 当发送FIN后，进入FIN_WAIT_1
   + FIN_WAIT_1
      - FIN_WAIT_2，当收到被动方ACK后进入
      - CLOSING，当收到被动方方FIN，并发送ACK后进入
      - TIME_WAIT，当收到被方FIN+ACK后进入，当FIN_WAIT_2收到FIN后进入，当CLOSING收到ACK后进入
 * 被动连接方
   + SYN_RCVD
   + ESTABLISHED
   + CLOSE_WAIT，当收到FIN,并发送ACK后进入
   + LAST_ACK，当发送FIN,后进入
   + CLOSE，当收到ACK后进入
 
### 
### 问题
 * 大量CLOSE_WAIT
   + 由于程序错误，没有关闭出现异常的连接
   + 通过配置参数缩短超时时间
 
 * 大量LAST_ACK
   + 由于主动方ACK包丢失
     - 重发fin
   + 由于主动方CLOSE
     - 此时直接LAST_ACK重传超时，进入CLOSE
   + 由于主动方商品被其它连接占用
     - 此LAST_ACK会再发送FIN关闭，此时主动会返回RST.最后LAST_ACK进入CLOSE
 
 * 大量TIME_WAIT
   + 服务端尽量不要主动关闭
   + 通过配置缩短TIME_WAIT时间
 
 * 大量FIN_WAIT_2
   + 通过配置超时，时间
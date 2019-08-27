## redis 主从复制

### 开始主从复制
 * 从服务器执行复制命令
   + slaveof 127.0.0.1 6379
   
### 2.8之前
 * 复制功能分为
   + 同步：将从服务器的数据库状态更新至主服务器当前所处服务器状态
   + 命令传播：当主服务器状态被修改，导致主从状态不一致时，让主从重新回到一致状态
   
 * 同步
   + slave向master发送sync命令完成
   + 过程
   1）slave向master发送sync命令
   2）master收到sync后执行bgsave命令，后台生成rdb文件，并使用一个缓冲区记录从现在开始执行的所有写命令
   3）当master的bgsave完成后，master会生成的rdb文件发送给slave，slave接收并载入这个rdb文件
   4）master将记录在缓冲区里的所有写命令发送给slave, slave执行这些写命令将自己数据库状态更新至master当前所处状态
   
 * 命令传播
   + 同步完成后，master与slave数据库达到一致状态
   + master将自己执行的写命令，也发送给slave执行。
   
 * slave断线重连
   + 当主从同步完后，过一段时间可能slave断线重连
   + slave重新发送sync命令，再执行一次同步 
  
 * 缺点：
   + 由于同步过，所在主从大部分数据是一致的。重连后再同步，又需要发送所有数据
   + master生成rdb操作会耗费大量cpu, 内存，磁盘io
   + master将rdb文件发送给slave生成耗费网络资源
   + slave载入rdb期间，会阻塞而没办法处理命令请求
   > 2.8之后使用PSYNC代替SYNC来避免这些缺点 
   
  

### 2.8之后
 * PSYNC命令
   + 完整重同步：与SYNC同步一样
   + 部分重同步：处理断线重连情况
   如果条件允许，master可以将主从连接断开期间执行的写命令发送给slave
   
 * 部分重同步实现
   + master的复制偏移量和slave的复制偏移量
   + master复制积压缓冲区
   + master运行id
   
 * 主从复制偏移量
   + 如果主从服务器处于一致状态，那么他们的偏移量总是相同的
   
 * 复制积压缓冲区
   + 是一个固定长度，先近先出队列，默认大小1mb
   + 配置：repl-backlog-size 1mb
   + slave执行psync时将偏移量发送给master
   + 如果偏移量的数据仍然存在于master缓冲区，主从执行部分重同步
   + 如果偏移量的数据不存在于master缓冲区，主人执行完整重同步
   + 大小计算
     - seconds * write_size_pre_second
     - seconds: 平均断线时间
     - write_size_pre_second: 主服务器平均每秒产生的写命令数量
     - 安全起见：2 * seconds * write_size_pre_second
     
 * master运行id
   + 主从初次复制时，master将id发送给slave
   + 当断线重连后，slave将id发送给master
     - 如果与master id 相同，则master尝试执行部分重同步
     - 如果与master id不相同，则执行完整重同步
     
 * PSYNC命令实现
   + PSYNC <runid> <offset>
   + 如果slave以前没有复制过任务master数据，则执行PSYNC ? -1，即执行完整重同步
   + 如果slave已经复制过某个master数据，则执行PSYNC <runid> <offset>
   收到的master判断应该对slave执行哪种重同步
   
 * PSYNC返回
   + master收到PSYNC命令的回复
   + +FULLRESYNC <runid> <offset>：执行完整重同步
   + +CONTINUE：执行部分重同步
   + -ERR：master版本低于2.8，识别不了PSYNC。执行完整重同步
   
 * 复制实现
   + 执行slaveof host port
   + slave会保存masterhost, masterport
   + slaveof是一个异步命令
   + 过程
     1. 建立连接
     2. slave向master发送ping命令，查检连接是否正常
       - 如果slave超时前没有收到回复，则重新创建连接
       - 如果master返回错误，断开连接，并重新创建连接
       - slave收到PONG正常
     3. 身份验证
       - 如果slave配置了：masterauth，则需要身份验证，如果没有配置也不需要身份验证
       - 如果master没有配置requirepass，slave没有配置masterauth，则复制工作可以继续进行
       - 如果master配置requirepass与slave配置的masterauth相同，则复制工作可以继续进行，否则失败
       - 如果master配置requirepass但slave没有配置masterauth，则返回no auth，失败
       - 如果master没有配置requirepass但slave配置了masterauth，则返回no password is set，失败
       - 失败后，重新发起连接，直到成功或退出
       
     4. 发送端口信息
       - slave执行replconf listening-port <port>
       - 向master发送slave监听的端口
       
     5. 同步
       - slave执行PSYNC
       - 此时master也会成为slave的客户端
       
     6. 命令传播
       - 同步完成后，master一直将自己执行的写命令发送给slave
       
 * 心跳检测
   + slave 每秒一次向master发送：REPLCONF ACK <offset>
      - offset：为复制偏移量
   + 作用
      - 检测主从连接状态
      - 辅助实现min-slaves选项
      - 检测命令丢失
   * 检测主从连接状态
      - master超过一秒未收到slave发送的心跳，则认为连接有问题
      执行：info replication查看连接延时时间：lag属性单位秒
      
   * 辅助实现min-slaves
      - 配置min-slaves-to-write 3
      - 配置min-slaves-max-lag 10
      - 在从服务器数量少于3个，或三个服务器的延迟（lag）值大于或等于10秒时，master拒绝执行写命令
      
   * 检测命令丢失
      - 由于网络故障，master传播给slave的命令半路丢失了
      - master发现slave offset小于自己复制偏移量，就会在积压缓冲区中找缺少的数据重新发送
      
       
   
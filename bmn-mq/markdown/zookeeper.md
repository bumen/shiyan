## zookeeper
 * 是一个典型的分布式数据一致性解决方案
 
### 实现功能
 * 数据发布/订阅
 * 负载均衡
 * 命名服务
 * 分布式协调/通知
 * 集群管理
   + 容错、负载均衡
 * Master选举
 * 分布式锁
 * 分布式队列
 * 配置文件集中管理
 * 集群的入口
 
### 特点
 * zookeeper是一个分布式程序
   + 只要半数以上节点存活，zookeeper就能正常服务
 * 为了保证高可用，最好用集群形态来部署zookeeper
 * zookeeper将数据保存在内存中
   + 保证高吞吐量和低延迟
 * zookeeper是高性能
   + 在读多于写时的高性能，因为写会导致所有服务器间同步状态
 * zookeeper有临时节点的概念
   + 当创建临时节点的客户端会话一直保持活动，临时节点就一真存在。
   + 当会话终结时，临时节点被删除。
   + 持久节点是指一旦这个Znode节点被创建，除非主动删除Znode，否则这个znode一直存在
 * zookeeper底层其实只提供了两个功能：
   + 管理（存储，读取）用户程序提交的数据
   + 为用户程序提供数据节点监听服务
   
 * 主种特点
   + 顺序一致性
     - 为每个更新请求，分配一个全局唯一递增编号
   + 原子性
   + 单一系统映像
     - 无论客户端连到哪个zk，获取数据都是一样的
   + 可靠性

### session
 * 指的是zookeeper服务器与客户端会话
 * 通过sessionTimeout设置session超时时间
 * 每个session都有一个sessionId, 保证全局唯一
 
### znode
 * znode-tree
 * 通过斜杠（“/"）来进行分割路径
 * 分为临时节点，与持久节点
 
### 版本
 * 每个znode都会有一个stat数据结构来维护版本
 * version  (当前znode的版本)
 * cversion (当前znode的子节点版本)
 * aversion (当前znode的ACL版本)
 
### Watcher
 * 事件监听器
 * 用户可以在指定节点上注册watcher, 当事件发生时，zookeeper会通知用户
 
### ACL accessControlLists
 * 权限控制
 * 五种权限
   + create：创建子节点
   + remove：删除子节点
   + read：获取节点数据和子节点权限
   + write：更新节点
   + admin：设置节点ACL权限
   
### 集群角色
 * Leader, Follower, Observer
 * Leader
   + 指供写与读
   + 更新系统状态
   + 负责投票的发起与决议
 * Follower
   + 只提供读
   + 在选举过程中参与投票
 * Observer
   + 只提供读
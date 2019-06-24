## server端
 * 使用的是nio
 
 

### IoAccepter
 * 作用
   + 服务端通过IoAccepter配置mina及启动。
   + 配置IoProcessor池大小
 * 主要接口
   + bind
   
 * registerQueue
   + ConcurrentLinkedQueue实现
   + bind时生成一个registerFuture
 
 * cancelQueue
   + ConcurrentLinkedQueue实现
   
 * executor默认不配置为cacheExecutor
   + 一般只会执行一个Acceptor线程
   
### Acceptor线程
 * 一个IoAccepter只会创建一个Acceptor线程
 * 当调用bind接口时，第一次会创建一个Acceptor线程
 * 作用
   + 执行ServerSocket bind 
   + accept连接
   + 创建IoSession
   + 初始化IoSession
   + 将IoSession分配到一个IoProcessor线程去执行
      - 分配策略：按sessionId % processor.length
      
      
### IoSession
 * 在ServerSocketChannel accept时创建，并初始化写消息队列，AttributeMap
   + 写消息队列是：ConcurrentLinkedQueue
   + AttributeMap: 是ConcurrentHashMap
   
 * 生成sessionId
   + 是一个static AtomicLong
   + 每创建一个sessionId累加一个
   
 * 创建filterChain
 
 * 获取IoHandler
   + 获取IoAccepter配置的IoHandler
 
### IoProcessorPool
 * executor默认是CacheExecutor
 * pool size
   + 获取IoAccepter配置的processor pool 大小
   
 * 创建IoProcessor
 
 
### IoProcessor
 * 负责处理客户端与服务器socket连接读写线程
 
 * 处理epoll bug
   + 如果selector 没有被唤醒，也没有selectionKey, 且select时间小于100 且连接10次。则发生epoll bug
   + 重新注册
   
 * newSessions
   + ConcurrentLinkedQueue实现
   + 生成一个IoSession后，就会放入NewSessions队列中。IoProcessor会消费这个队列
   将IoSession注册到Processor selector
   + build filter chain
     - 跟据IoAccepter filter chain builder 构建session的filter chain
   
   + 同时触发filterChain的fireSessionCreated， fireSessionOpened事件  
   
 * 处理read事件
   + 读取数据，触发filterChain的fireMessageReceived
   + 如果读到-1。触发filterChain的fireInputClosed
   
 * 处理write事件
   + 可写时，将session放入flushingSessions-ConcurrentLinkedQueue中
 
 * flush
   + 写flushingSessions中的session
   + 触发filterChain的fireMessageSent
   
 * 执行notifyIdleSessions。
   + 每一秒执行一个检查所有session空闲状态
   
 * 执行notifyWriteTimeout
   + 将当前在正执行的写请求是否超时
   + 如果超时触发filterChain的fireExceptionCaught
   
 * 执行removeSessions
   + 删除无效session
  
   
  
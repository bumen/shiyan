## client端
 * 使用的是nio
 

### IoConnector
 * 作用
   + 服务端通过IoConnector配置mina及启动。
   + 配置IoProcessor池大小
 * 主要接口
   + connect
   
 * connectQueue
   + ConcurrentLinkedQueue实现
   + connect时生成一个connectRequest
 
 * cancelQueue
   + ConcurrentLinkedQueue实现
   
 * executor默认不配置为cacheExecutor
   + 一般只会执行一个Connector线程
   
### Connector线程
 * 一个IoConnector只会创建一个Connector线程
 * 当调用connector接口时，第一次会创建一个Connector线程
 * 作用
   + 执行ServerSocket connect 
   + finish connect连接
   + 创建IoSession
   + 初始化IoSession
   + 将IoSession分配到一个IoProcessor线程去执行
      - 分配策略：按sessionId % processor.length
      
### IoSession处理与server端一致
      

  
## IoService

### IoAcceptor
 * 服务端使用
 * 确定IoProcessorPool
 * 一个Acceptor就是一个selector即一个ractor。不断循环accept新的连接进来
   + 一个Acceptor可以绑定多个地址
   + 为每个session分配一个processor
   

### IoConnector
 * 客户端使用

### IoProcessor
 * 每个session都会被分配到一个processor线程去处理


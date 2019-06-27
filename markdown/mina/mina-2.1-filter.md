## filter chain 实现

### IoFilterChainBuilder
 * IoService中定义了IoFilterChainBuilder
   + 默认实现DefaultIoFilterChainBuilder
   
 * 作用
   + 通过获取IoFilterChainBuilder，来自定义项目相关Filter
   + 构建IoFilterChain
   
 * 因为IoAcceptor, IoConnector都继承是IoService
   + 它们都具有IoFilterChainBuilder
   
 * 当创建IoSession后
   + 在将IoSession绑定到IoProcessor时，跟据IoFilterChainBuilder来构建IoSession的IoFilterChain
   
   
### IoFilterChain
 * 创建IoSession时，会创建IoFilterChain
   + 默认实现DefaultIoFilterChain
   + 会关联IoSession
   
 * HeadFilter
   + 处理写消息到消息队列
      - 通知processor消费消息
   + 处理关闭连接
      - 将session写入removeQueue
      - 通知processor处理关闭连接接口
 * TailFilter
   + 将processor发出的事件，通知到IoHandler
   
 
 * fireSessionCreated
   + 从head执行
   + 在processor run的时候 处理绑定selector后。session绑定到selector后执行
 * fireSessionOpened
   + 从head执行
   + 在processor run的时候 处理绑定selector后。session绑定到selector后执行
   
 * fireSessionClosed
   + 从head执行
   + 在processor run的时候 removeSession时，触发
   
 * fireSessionIdle
   + 从head执行
   + 在processor run的时候，处理完读取后会检查idle
   
 * fireMessageReceived
   + 从head执行
   + 在processor run的时候 read到字符后，触发
 
 * fireMessageSent
   + 从head执行
   + 在processor run的时候 发送一个消息后，触发
 
 * fireExceptionCaught
   + 从head执行
   + 在processor 处理过程的异常
 
 * fireInputClosed
   + 从head执行
   + 在processor run的时候 处理读时，读到-1触发
 
 * fireFilterWrite
   + 从tail执行
   + 在IoSession write一个消息时，触发
 
 * fireFilterClose
   + 从tail执行
   + 在IoSession closeNow时，触发
 
 * fireEvent
   + 从head执行
   + 自定义事件
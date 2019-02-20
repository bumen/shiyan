## channel解析


### register第一步注册
 * 注册功能主要就是为channel分配线程
 * 同时socketchannel会注册到selector上，但不会监听任务事件
 * 同时channel.registered属性置为true
 * 同时pipeline.invokeHandlerAddedIfNeeded();
    + pipeline.registered属性置为true
    + 此时ChannelInitializer.handlerAdded方法被调用，即把自定义handler注册到pipeline。
 * 同时注册promise.success
 * 同时触发注册事件pipeline.fireChannelRegistered();
 
### bind, 如果是server端第二步绑定
 * 通过触发bind事件，通过pipeline传递，并最终执行
 * 会进行socket绑定操作
 * 通过invokeLater()调用fireChannelActive
 * 同时promise会设置成功

### connect, 如果是client端第二步连接
 * 通过触发connect事件，通过pipeline传递，并最终执行
 * 会进行socket连接
 * 如果连接成功，promise会设置succes。如果promise设置succes失败说明promise被取消，需要关闭连接
 会调用fireChannelActive
 * 如果未连接成功，且配置了ConnectTimeoutMillis,则会启动定时任务去监听连接结果
 最后等到finishConnect()接口被调用后再次判断是否连接成功
 
 
### diconnect，与connect接口是一对
 * 会断开connect操作
 * 关闭scoket连接
 * 通过invokeLater()调用fireChannelInactive
 
### close
 * 如果solinger > 0则，只先取消注册但不关闭连接，意思是等待网络上的消息发完再关闭
 通过invokeLater()调用deregister
 * 否则直接关闭连接，判断如果是flush异常关闭则，通过invokeLater()调用deregister
 否则直接调用deregister
 * 关闭时什么清除outboundbuffer中的所有消息

### deregister
 * 通过invokeLater()调用doDeregister
 如果是关闭连接时调用的deregister，则需要先调用fireChannelInactive
 并调用fireChannelUnregistered
 * 取消注册只是将socket从selector上取消，但还在当前eventloop上
 只是不能处理写事件，但可以处理关闭，disconnect等

### read
 * 如果socket是有效连接，则会设置readInterestOp
 
### write(msg)
 * 先判断msg类型，如不符合对应channel可以发送的消息类型时，此次发送失败
 如：niochannel发送ByteBuf, 与FileRegion类型，如果ByteBuf不是directBuf时，会转为directBuf，实现发送时0拷贝
 所以注意发送前需要将消息转码为ByteBuf
 * 将消息outboundBuffer
 * 注意serverchannel不支持写操作

### flush
 * 如果连接断开，则会丢掉将要flushed消息，会把消息从outboundbuffer的flushed链表删除
 * 如果写的过程中抛出异常，如果配置了自动关闭则会关闭连接，否则会关闭output通道
 同时会当outboundbuffer中的所有要发送的消息删除
 * 写正常时，会根据配置的getWriteSpinCount次数，一次写-spin次消息。如果没有可以也会退出
 如果写过程中，不可写时，会注册写事件
 
### writeAndFlush
 * 分为两步，第一步先执行write. 从TailContext->HeadContext
 * 第二步再执行flush. 从TailContext->HeadContext
 
### isWritable()接口使用
 * 与highWaterMask, lowWaterMask有关
 * 当write时，会增加pendingSize如果size大于higt则writeable=false
 * 当flush后，会减少pendingSize如果size小于low则writeable=true
 * 通过该接口可以观察写的压力
 
### 实现ChannelOutboundHandler接口的使用
 * 当调用channel中实现了ChannelOutboundHandler的接口时，都会转发到pipeline中
 因此，都是通过piepline调用，并从TailContext一直向上传递调用
 
### invokeLater(test)接口使用
 * 当在outbound接口调用中触发了inbound接口时，需要延时执行
 避免inbound中调用outbound，然后outbound又触发了inbound
 ``` 
  eventLoop().execute(task);
 ```
 
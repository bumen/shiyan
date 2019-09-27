### 事件处理

## channel pipeline 相关实现与使用

### 实现

#### pipeline 使用
 * 是一条包括inbound<->outbound的链表

1. 添加handler
 * addLast(handler) 
 * 是同步方法与下列方法互斥
   + 主要是同步，ctx链表更新，registered属性值，pendingHandlerCallbackHead属性
   + remove
   + replace
   + destroy
   + destroyDown
   + callHandlerAddedForAllHandlers
   
 * new DefaultChannelHandlerContext(pipeline, childExecutor(group), name, handler);
   + 创建新的ctx
   + childExecutor参数：
      - handler的执行线程
      - 如果group == null, 默认使用channel的executor, 即io线程 
      - 如果group != null, 可以为所有指定同相group的所有handler使用同一个线程，也可以每次添加时随机一个线程
      
 * 连接到pipeline head<->ctx<->tail链表上
 
 * registered 属性判断
   + registered = true, 表示channel注册成功
   + 如果true, 则调用callHandlerAdded0()方法
   + 如果false, 则先将ctx。链接到pendingHandlerCallbackHead待处理链表上。等待注册成功后，再调用callHandlerAdded0()
   
 * callHandlerAdded0()方法
   + 死循环设置handler handlerState属性
   + 设置handlerState属性成功后
   + 在chileExecutor线程中执行handler.handlerAdded方法。如果此方法抛出异常则执行remove(ctx)
   + handlerAdded方法执行失败
      - 将ctx从head<->ctx<->tail链表上删除
      - 执行handler.handlerRemoved。如果抛出异常将被ignore
      - 执行fireExceptionCaught通知handlerAdded方法抛出的异常信息
      
      
2. 删除handler
 * remove(ctx)
   + 与添加handler同步互斥
 * 断开连接 pipeline Head<->ctx<->tail
 * registered属性判断
   + 如果查true, 调用callHandlerRemoved0()
   + 如果是false, 创建PendingHandlerRemovedTask添加到pendingHandlerCallbackHead链表中。等待注册成功后，再调用callHandlerRemoved0()
 * callHandlerRemoved0()
   + 调用handlerRemoved(ctx)
   + 设置handlerState属性为removed状态
   + 如果handlerRemoved(ctx)抛出异常，则执行fireExceptionCaught通知异常信息
   

3. destroy()
 * 什么时候被调用
   + netty框架调用
   + user调用
 * 先从heap一直找到tail。然后从tail开始向下查找每一个ctx, 然后在ctx线程执行remove(ctx)操作
   + 其中destroyUp()中的异步处理是为了避免与add等方法保证线程同步。避免链表不是最新的
   + 其中destroyDown()中的异常处理是为了切换到ctx线程为执行remove(ctx)

#### handler
 * handlerAdded(ctx)
 * handlerRemoved(ctx)
 
#### outBound与Inbound
 * 是以netty框架做为参照物来命名。类似io的inputStream, outputStream
 
 * 相对netty来说
   + outBound就是向netty内提供数据，即用户可以通过out的write写数据给netty
   + inBound就是向外提供数据。即用户可以通过in读取netty内部数据
  
 * 相对netty框架与用户关系来说。类似tcp协议栈
   + netty框架是底层，用户为上层
   + head是从底向上反数据给用户，数据流up
   + tail是从上层向底层流数据给netty, 数据流down
   
   + 所在在pipeline.destroy()时，有destroyUp()和destroyDown()
   
 * head
   + inbound作用：可以在各个inbound接口调用开头做一些过滤拦截处理。类似aop原理
   + outbound作用：数据流down最终流向处理
 * tail
   + inbound作用：数据流in最终流向处理
   + outbound作用：其实没有实现outbound接口。实现的是ChannelOutboundInvoker接口，该接口作用是为user使用。因为user拿不到ctx属性
   + ChannelOutboundInvoker接口其实是对ChannelOutboundHandler一种代理或适配。适配给user使用。然后适配后再转为调用ChannelOutboundHandler
   

#### out-bound
 * 用户可以调用的，对外开放的接口。是netty框架向外提供的服务接口，用来向netty发送数据
 * bind(ctx, localAddr, channelPromise)
 * connect(ctx, remoteAddr, localAddr, channelPromise)
 * disconnect(ctx, channelPromise)
 * close(ctx, channelPromise)
 * deregister(ctx, channelPromise)
 * read(ctx)
 * write(ctx, msg, channelPromise)
 * flush(ctx)
 
#### in-bound
 * 用户不可以调用，对内开放的接口。是netty框架内部调用的接口，用来通知用户数据
 * channelRegistered(ctx)
 * channelUnregistered(ctx)
 * channelActive(ctx)
 * channelInactive(ctx)
 * channelRead(ctx, msg)
 * channelReadComplete(ctx)
 * userEventTriggered(ctx, evt)
 * channelWritabilityChanged(ctx)
 * exceptionCaught(ctx, cause)
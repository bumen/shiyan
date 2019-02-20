### pipeline代码
 * 向pipeline中添加或删除handler是可能触发运行时异常

#### pipeline.callHandlerAdded0(ctx) 方法
 1. ctx当前状态改为ADD_COMPLETE
   + 且当前状态不能是REMOVE_COMPLETE
 2. 执行handler.handlerAdded(ctx)
   + 如果抛出异常
   + 从pipeline链表删除ctx
   + 执行handler.handlerAdded(ctx)
   + ctx状态改为REMOVE_COMPLETE
   + 执行pipeline.fireExceptionCaught()
   
#### addFirst
 * addLast相似只是第4步不同
 4. 将handlercontext加到pipeline链表正数第二位置

#### addLast
 1. 判断handler是否已经添加过
   + 如果是ChannelHandlerAdapter子类，不是sharable类型则只能添加一次。
 2. 生成handler名子唯一
   + 如果未指定名称，使用handler对象生成名称
   + 如果指定名称，需要判断名子是否已经被已经添加的handler使用
 3. 创建handlercontext
 4. 将handlercontext加到pipeline链表倒数第二位置
 5. 调用handler.handlerAdded(ctx)方法
   + 此方法执行要保证使用pipeline注册的eventexecutor线程去调用。所以
   + 如果pipeline已经注册到eventexecutor则调用callHandlerAdded0(ctx)
   + 如果pipeline未注册，则先把ctx添加pipeline的pendingHandlerCallbackHead链表中。等注册完再调用执行
   + 同时ctx状态改为ADD_PENDING
   
 * 当注册成功后，先执行pendingHandlerCallbackHead链表，然后再fireRegister
   
* remove
 * 可以在handler执行中调用删除当前handler
 删除后还只是从pipeline链表中删除。看还保留着prev与next


#### destroy()
 * 处理unregister事件结果后，如果channel.isopen=false，则会删除掉所有handler
 从TailContext->HeadContext删除。删除时掉用handerRemove方法

#### pipeline中的inbound与oubound
 * pipeline调用inbound是从HeadContext开始向下，一直到TailContext后（中间可能会断断不fire，则到不了TailContext)
 TailContext是一个收尾工作。最后再逐层回溯到HeadContext
 * pipeline调用outbound是人TailContext开始向上
 * inboundHanlder一直向下直到TailContext，结束
 * outboundHandler一真向上直到HeadContext，再调用channel的oubound方法
 
 
#### AbstractChannelHandlerContext
 * 上下文作用：
   + 每添加一个handler则创建一个对应handlercontext
   + netty通过调用handlercontext来执行用户handler
   + 用户handler中可以使用handlercontext获取netty pipeline
 * 状态
   + ADD_PENDING
   + ADD_COMPLETE
   + REMOVE_COMPLETE
   + INIT
   
 * 可以指定fire等方法调用时的线程即executor
   + 在创建时指定executor,(即添加handler时)
   + 如果channel.config()中ChannelOption.SINGLE_EVENTEXECUTOR_PER_GROUP为true
   + 则创建handler指定相同的eventGroup则使用相同的线程执行
   + 否创建handler指定相同的eventGroup则使用不同的线程执行，每一个ctx使用线程池中的一个线程
   + 如果没有指定executor则，使用channel.eventLoop()线程
   + 注意还是不要指定线程池。就使用默认channel.eventloop。避免不熟悉源码造成使用时的错误
   + 即如果配置eventGroup，则一条pipeline的ctx链上，可能不同ctx使用不同线程执行
   
#### HeadContext是outbound类型
 * 当通过ctx调用outbound接口时，最终会调到HeadContext中的方法
 * HeadContext中outbound接口方法，会调用channel.unsafe()中的outbound方法
 * pipeline中的outbound接口调用，都会从TailContext开始一直向上
   + + 所以不要在outboundHandler中使用pipeline调用outbound接口，会死循环
 * ctx中的outbound接口调用，是从当前Context开始一直向上
 
#### HeadContext的channelRegistered
 * 先执行pendingHandlerCallbackHead链表
 * 再执行fireChannelRegistered
 
#### HeadContext的channelActive
 * 先执行fireChannelActive
 * 如果channel配置为autoRead，则会处理发自动读取操作
 
#### HeadContext的channelReadComplete
 * 先执行fireChannelReadComplete
 * 如果channel配置为autoRead，则会处理发自动读取操作

#### TailContext是inbound类型
 * 当通过ctx调用inbound接口时，最张会调用到TailContext中
 * TailContext中的inbound接口方法，有的什么也不处理，有的只是释放对象
 * pipeline中的inbound接口调用，都会从HeadContext开始一直向下
   + 所以不要在inboundHandler中使用pipeline调用inbound接口，会死循环
 * ctx中的inbound接口调用，是从当前Context开始一直向下
 
#### writeAndFlush(msg)与writeAndFlush(msg, promise)
 * writeAndFlush(msg)会生成一个新的promise。意思作为一个新的消息发送
 * writeAndFlush(msg, promise)可以继续使用上个handler中创建的promise只要这个promise没有确认过
   + 如果确认，则表示前一个hander发送的消息在当前hander被确认了。
   如果使用writeAndFlush(msg, promise)继续发送确认的promise将抛出异常
   一般通过配置promise.listener来通知前一个handler消息被确认
   + 要想在接着发送这个消息，需要使用writeAndFlush(msg)，作为新消息发送。
 * 对象有顺序要求的消息。不要使用不同线程发送消息。
   + 如消息A在handler中使用ctx.write(A), 因为handler中通常默认是channel线程 
   消息B在别一个线程使用ctx.write(B)发前。虽然最终都由handler中统一线程执行
   
 * bind(args), connect(args), disconnect(), close(), deregister()
   + 同样有bind(args, promise), 
   + connect(args, promise), disconnect(promise), close(promise), deregister(promise)
   + 如果promise被确认，将不能再发送
 * 对outbound接口的调用，都要注意多线程调用的顺序问题
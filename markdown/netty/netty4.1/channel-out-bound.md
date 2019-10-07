## out-bound-handler

### register
 * 执行bind, connect接口时，会先注册channel注册。channel同时只能注册一次，但可以重复注册
 * 目的
   + 将socket注册到某个selector上
 * 重复注册
   + 先要执行deregister, 解除之前的注册，再调用bind或connect实现重复注册
   
 * 实现
   + 判断注册状态，如果已经注册了就返回
   + 在要注册到的eventloop线程中，执行注册任务
   + 注册socket 到selector。 
     > 注意：只是注册，不关心任何事件
     
   + 判断是否需要初始化pipeline handler链表
     > 注意：bind时，添加一个Acceptor handler
     
   + 通知promise 注册成功
   + 发出Registered事件
   + 判断socket有效状态
      - server socket 判断是否bind成功
      - client socket 判断是否connect成功
      - 状态有效，则首次成功，则发送Active事件。一般不会成功，因为是注册之后执行bind或connect操作。
      channel第一次注册才会判断，重复注册也不会判断
      - 如果已经成功，则判断是否开始读操作
   + 注册结束
   
 * 实现总结
   + 完成socket注册
   + 触发register事件
   + 重复注册处理
   
 * 注册过程中失败处理 
   + 关闭socket 连接
   + closeFuture.close
   + 通知promise注册失败
   + 如果通过bootstrap注册时失败，可能会执行pipeline close
   
### bind
 * 注册成功后，才进行端口绑定操作
 * 实现
   + 添加bind任务，交给eventloop线程处理（即注册线程）
   + 执行socket bind
      - 如果绑定失败，如：端口被占用。则返回失败
      
   + 如果active状态打开，则添加fireChannelActive任务，下次心跳执行fireChannelActive事件
   + 通知注册成功 
  
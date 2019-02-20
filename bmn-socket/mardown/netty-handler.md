## handler解析


### ChannelInitializer
 * initChannel(Channel)
   + 该方法是在handlerAdded(ctx)被调用时执行
   + 执行完成后自动将本hander从pipeline中删除
   + ServerBootstrap和向添加child-pipeline添加handler时会使用
   当处发时执行时此时pipeline的register状态为false，所以会在HeadContext.channelRegistered被执行时，才会被调用
   ,此时即时删除后，但还保留着next。所以再通过ctx.fireChannelRegistered还会继续下传。
   保证通过initChannel方法中添加的handler不会错过fireChannelRegistered事件
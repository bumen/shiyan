## event executor
 * 不能restart
 
 
### run方法
 * 处理任务与selector
 * 没有任务也没有io事件，则selector.select()阻塞
 * 有任务没事件，则打断selector阻塞，去处理任务
 * 处理任务
 * 处理io事件
 
### task 任务
 * 调度任务
 * 普通任务
   + pipeline handler处理的业务
   
### io事件
 * finishConnect
 * forceFlush
 * read
 * close
 
### shutdown与shutdownGracefully
 * shutdown后，run退出时，只会执行一次任务
 * shutdownGracefully，run退出时，有一次quiet time执行时间
 
## java nio
 * http://stenographist11.rssing.com/chan-7008770/all_p805.html

### socket nio 
 * ServerSocketChannel配置非阻塞
 * Selector注册channel后，成为异步非阻塞
   + 是事件驱动
 
#### SocketChannel read
 * 非阻塞
 * 返回值
   + 大于0：读到字节
   + 等于0：没有读到字节，非阻塞正常
   + 等于-1：链路已经关闭，需要关闭SocketChannel，释放资源 
   
#### SocketChannel write
 * 非阻塞
 * 写一次并不能保证一次能把需要发送的字节数组发送完。
 此时出现写半包，我们需要注册写操作，不断轮询Selector将没有发送完的数据发送完成
### zeroMq
 * [参考](https://zguide.zeromq.org/docs/chapter2/)
 * 版本3.3
 
### 为什么使用
 * 自动重连
 * 

#### 使用
 * 代理和桥实现
   + 都可以使用ZMQ.Proxy
   + 只能代理这些patterns
   ``` 
     you would usually stick to ROUTER/DEALER, XSUB/XPUB, or PULL/PUSH
   ```
      - SUB/PUB 由 XSUB/XPUB实现代理
      - REQ/REP 由 ROUTER/DEALER实现代理
 
 * Handling Erros and ETERM
   + ZeroMQ’s error handling philosophy is a mix of fail-fast and resilience.
   + Follow POSIX conventions 
   ``` 
    Methods that create objects return NULL if they fail.
    Methods that process data may return the number of bytes processed, or -1 on an error or failure.
    Other methods return 0 on success and -1 on an error or failure.
    The error code is provided in errno or zmq_errno().
    A descriptive error text for logging is provided by zmq_strerror().

   ```
   + There are two main exceptional conditions that you should handle as nonfatal:
      - When your code receives a message with the ZMQ_DONTWAIT option and there is no waiting data, 
      ZeroMQ will return -1 and set errno to EAGAIN.
      - When one thread calls zmq_ctx_destroy(), and other threads are still doing blocking work, 
      the zmq_ctx_destroy() call closes the context and all blocking calls exit with -1, and errno set to ETERM.

   
 * interrupt singles
 ``` 
 1. If your code is blocking in a blocking call (sending a message, receiving a message, or polling), 
 then when a signal arrives, the call will return with EINTR.
 2. Wrappers like s_recv() return NULL if they are interrupted.
 3. So check for an EINTR return code, a NULL return, and/or s_interrupted.
    
 ```
 * ZeroMQ context是线程安全的
 * ZeroMQ socket是线程不安全的
 
 * 线程之间使用信号通知
    + SocketType.PAIR
       - 静态的；不会自动重连
    + protocol: inproc
    
 * 进程之间信号通知
    + 通过REQ/REP
    
 * HWM 
    + default
    ``` 
        In ZeroMQ v2.x, the HWM was infinite by default. 
        This was easy but also typically fatal for high-volume publishers. 
        In ZeroMQ v3.x, it’s set to 1,000 by default
    ```
    + buffer
   ```` 
   
   ZeroMQ uses the concept of HWM (high-water mark) to define the capacity of its internal pipes. 
   Each connection out of a socket or into a socket has its own pipe, and HWM for sending, 
   and/or receiving, depending on the socket type. 
   Some sockets (PUB, PUSH) only have send buffers. 
   Some (SUB, PULL, REQ, REP) only have receive buffers. 
   Some (DEALER, ROUTER, PAIR) have both send and receive buffers.
   ````
    + reject strategy
    ``` 
    When your socket reaches its HWM, it will either block or drop data depending on the socket type. 
    PUB and ROUTER sockets will drop data if they reach their HWM, 
    while other socket types will block. Over the inproc transport, 
    the sender and receiver share the same buffers, 
    so the real HWM is the sum of the HWM set by both sides.
    ```
    + exact
    ``` 
        the HWMs are not exact
        the real buffer size may be much lower (as little as half), 
        due to the way libzmq implements its queues.
    ```
 
### REQ-REP
 * REQ-REP是成对出现，同步阻塞的
 * 添加一个中间路由
    + ROUTER<->DECLARE
    + ROUTER是接收消息后为每个消息生成一个消息头，记录到路由表中
       - 同时将REQ消息放到公平队列中
    + DECLARE
       - 将ROUTER消息分发给所有REP
       - 将REP消息放到公平队列中  
    
 * 添加多个中间路由，类似
   + 每次收到请求消息ROUTER都会加一个头
   + 每次收到响应消息ROUTER都会去掉一对应头，找到路由信息，将消息返回
   
 * ROUTER添加的头标识
   + v2.2之前用的UUID，之后用的5byte（0+ a random 32bit integer）
   + 每个请求连接一个标识 
   
 * 回顾
   + REQ: 同步，阻塞。如果REQ socket连接多个地址，则请求将被分发每个peers，同时等待响应。
   + REP: 同步，阻塞。如果REP socket连接多个地地，则公平读取每个peers，同时处理完就返回响应。
   + DECLARE: 是异步，在所有连接之间分发要发送的消息。将收到的消息加入公平队列
   + ROUTER: 是异步，为什么个连接创建标识，将标识添加到消息中发给调用都。收到消息时通过标识找到连接再转发出去。
   
 * DECLARE to REP 结合使用
   + DECLARE是异步的，可以与多个REP通信
   + DECLARE必须模拟REQ的请求消息
      - 发送消息时需要先发送一个空消息，（参考REQ发的消息内容，因为REP收消息时是按REQ发的消息格式处理消息的，否则丢掉消息）
  
 * REQ to ROUTER 结合使用
   + ROUTER是异步的，可以同步处理多个请求
   + ROUTER有两种使用方式 
      1. 做为代理转发消息
      2. 做为应用，处理消息并返回响应
         - 要返回对应REQ可以解析的响应格式内容
         
 * DECLARE to ROUTER 结合使用
   + 两都都可完全异步通信，同时消息格式可以自定义
   
 * DECLARE to DECLARE 结合使用
   + 很少使用
   
 * ROUTER to ROUTER 结合使用
   + 应该避免使用，属于高级应用
   
 * ROUTER socket 详细内容
   + 每个端可以先连接ROUTER, ROUTER也可以先连接peer，都一样
   + 每个peer可以通过ZMQ_IDENTITY 设置一个连接标识
   + 在连接时候，the peer 告诉 ROUTER使用它设置的连接标识（如果设置了）
   + 如果the peer没有告诉使用指定连接标识，则ROUTER自动生成一个
   
 * ROUTER 错误处理
   + 默认是对于发送失败的消息，它是默默丢掉
   + v3.2之后，提供了设置ZMQ_ROUTER_MANDATORY。当发现路由失败的消息时会触发EHOSTUNREACH 错误
   
 * 
   
   
   
   
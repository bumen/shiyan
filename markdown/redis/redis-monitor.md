## redis monitor监视器

### monitor命令
 * 可以将一个客户端变为一个监视器，实时接收并打印出服务器当前处理的命令请求
   + 每当一个客户端向服务器发送一条命令时，服务器除了会处理这条命令请求之外，还会将关于这条命令请求的信息发送给
   所有监视器
   
### 成为监视器
 * 伪代码
 ```
    def MONITOR():
       # 打开客户端监视器标志
       client.flags |= REDIS_MONITOR
       # 将客户端添加到服务器状态的monitors链表的末尾
       server.monitors.append(client)
       # 向客户端返回
       send_reply("ok")
 ```
 
### 向监视器发送命令
 * 服务器在每次处理命令请求之前，都会调用replicationFeedMonitors函数，由这个函数将被处理的命令请求相关信息发送给
 各个监视器
 * 伪代码
 ```
    def replicationFeedMonitors(client, monitors, dbid, argv, argc):
        # 根据执行命令的客户端，当前数据库的号码，命令参数，命令参数个数等参数
        # 创建要发送给各个监视器的信息
        msg = create_msg(client, dbid, argv, argc);
        # 遍历所有监视器
        for monitor in monitors:
            # 发送消息给监视器
            send_message(monitor, msg);
      
 ```
 
 
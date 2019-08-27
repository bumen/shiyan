## redis慢查询日志
 * 记录执行时间超过给定时长的命令请求
 
### 配置
 * slowlog-log-slower-than
   + 指定执行时间超过多少微秒（1秒等于1000 000微秒）的命令请求会被记录到日志上
   
 * slowlog-max-len 
   + 指定服务器最多保存多少条慢查询日志
   + 如果达到上限，在添加一条新的慢查询日志前，会先将最旧的一条慢查询日志删除
   
 * 测试命令
   + config set slowlog-log-slower-than 0
      - 记录所有命令
   + config set slowlog-max-len 5 
      - 最大保存5条
      
### 查看
  * 命令
   + slowlog get 获取所有
   ``` 
   1) 1) (integer) 4            # 日志的唯一标识符
      2) (integer) 1387871447   # 命令执行时的unix时间
      3) (integer) 13           # 命令执行时长，以微秒为单位 
      4) 1) "set"               # 命令以及命令参数
         2) "database"
         3) "redis"
   ```
   + slowlog get [number] 
      - number 获取几条
      
   + slowlog reset
      - 清除
      
   + 伪代码
   ``` 
        def slowlog_get(number=None):
            # 用户没有指定长度
            # 那么打印服务器包含的全部日志
            if number is None:
                number = slowlog_len()
            
            # 遍历服务器中慢查询日志
            for log in redisServer.slowlog:
                if number <= 0:
                    # 打印的日志数据已经足够，跳出循环
                    break;
                else
                    number -= 1;
                    
                printLog(log)
                
        def slowlog_len():
            return len(redisServer.slowlog)
            
        def slowlog_reset():
            for log in redisServer.slowlog:
                deleteLog(log)
   ```
  
      
### 慢查询记录的保存
 * 服务器状态 包含了几个和慢查询日志功能有关的属性
 * 伪代码
 ```
    struct redisServer{
        # 下一条慢查询日志id
        long long slowlog_entry_id;
        # 保存了所有慢查询日志的链表
        list *slowlog;
        # 配置slowlog-log-slower-than选项值
        long long slowlog_log_slower_than;
        # 配置slowlog-max-len选项值
        unsigned long slowlog_max_len;
    }
 ```
 * slowlog_entry_id: 初始始为0， 每当创建一条慢查询日志时，这个属性就会作为新日志的id, 同时自增
 * slowlog链表值
   + 每次插入，都将最新的插入到表头
 ``` 
    typedef struct slowlogEntry {
        # 唯一标识符
        long long id;
        # 执行时间unix
        time_t time;
        # 执行时间，微秒
        long long duration;
        # 命令与参数列表
        robj **argv;
        # 参数个数
        int argc;
    }
 ```
 
### 添加日志
 * 伪代码
 ``` 
    # 命令执行前，记录时间
    before = uninxtime_now_in_us()
    # 执行命令
    execute_command(argv, argc, client)
    # 命令执行后，记录时间
    after = uninxtime_now_in_us()
    # 判断是否需要记录慢查询日志
    slowlogPushEntryIfNeeded(argv, argc, after-before)
    
    
    void slowlogPushEntryIfNeeded(robj **argv, int argc, long long duration) {
        # 慢查询功能未开启，直接返回
        if(server.slowlog_log_slower_than < 0) return;    
        
        # 如果执行时间满足，则添加慢查询日志
        if(duration >= server.slowlog_log_slower_than) 
            listAddNodeHead(server.slowlog, slowlogCreateEntry(argv, argc, duration))
        
        // 如果日志数量过多，那么进行删除
        while(listLength(server.slowlog) > server.slowlog_max_len)
            listDelNode(server.slowlog, listLast(server.slowlog))
    }
 ```
## redis 事务

### 事务特性ACID
 * 原子性，一致性，隔离，持久性
 
### 事务实现
 * 一个事务开始到结束通常会经历三个阶段
   + 事务开始
   + 命令入队
   + 事务执行
   
 * 事物开始
   + multi命令的执行标志事务的开始
   + 客记端从非事务状态切换至事务状态，通过设置客户端flags属性打开REDIS_MULTI标记
   + 伪代码
   ``` 
        def multi():
            client.flags |= REDIS_MULTI;
            send_reply("ok")
   ```
   
 * 命令入队
   + 当一个客户端处于非事务状态时，这个客户端发送的命令立即被服务器执行
   + 当一个客户端处于事务状态时，根据不同命令执行不同操作
     - 当发送命令为exec, discard, watch, multi其中一个，那么服务器立即执行这个命令
     - 其它以外命令，则将这个命令放入事务队列，然后向客户端返回QUEUED回复
     
 * 事务队列
   + 每个客户端都有自己的事务状态
   + 伪代码
   ``` 
        typedef struct redisClient{
            # 存放事务状态
            multiState mstate;
        }
        
        typedef strcut multiState{
            # 事务队列，FIFO
            multiCmd *commands;
            # 已入队命令计数
            int count;
        }
   ```
   + 事务队列是一个multiCmd类型的数组，数组中的每个multiCmd结构都保存了一个已入队列命令的相关信息
     - 是一个先进先出队列
   + multiCmd伪代码
   ``` 
        typedef struct multiCmd{
            # 参数
            robj * argv;
            # 参数个数
            int argc;
            # 命令指针
            strcut redisCommand *cmd;
        }
   ```
   
 * 执行事务
   + 当一个处于事务状态的客户端发送exec命令时，这个命令被立即执行。服务器遍历这个客户端的事物队列，最后将执行的全部
   结果返回给客户端
   + 伪代码
   ``` 
        def exec():
            # 创建空回复队列
            reply_queue=[]
            # 遍历所有命令
            for argv, argc, cmd in client.mstate.commands:
                # 执行命令
                reply = execute_command(cmd, argv, argc);
                # 保存返回
                reply_queue.append(reply)
                
            # 移除事务标记，让客户端回到非事务状态
            client.flags &= -REDIS_MULTI;
            
            # 清空事务状态
            # 释放队列
            client.mstate.count = 0
            release_trancaction_queue(client.mstate.commands)
            # 将事务执行结果返回给客户端
            send_reply_to_client(client, replay_queue)
   ```
   
### WATCH实现
 * watch命令是一个乐观锁，它可以在exec命令执行前监视任意数量的数据库键，并在exec命令执行时，检查被监视的键是否至少
 有一个已经被修改过了，如果是的话，服务器将拒绝执行事务。并向客户端返回事务执行失败的空回复
 * 使用watch命令监视数据库键
   + 每个数据库都保存着一个watched_keys字典，这个字典的键是某个被watch命令监视的数据键，而字典值是一个链表，
   链表记录了所有监视相应数据库键的客户端
   + 伪代码
   ``` 
        typedef struct redisDb{
            dict *watched_keys;
        }
   ```
   
 * 监视机制的触发
   + 所有对数据库进行修改的命令，在执行后都会调用touchWatchKey函数对watched_keys字典进行检查，查看是否有客户端下在
   监视刚刚被命令修改过的数据库键。如果有那么将监视的客户端的REDIS_DIRTY_CAS标识打开，表示事务安全性被破坏
   + 伪代码
   ``` 
        def touchWatchedKey(db, key):
            # 判断key是否被监视
            if key in db.watched_keys:
                # 遍历所有客户端
                for client in db.watched_keys[key]:
                    # 打开标记
                    client.flags |= REDIS_DIRTY_CAS;
   ```
   
 * 判断事物所是否安全
   + 当服务器收到一个exec命令时，会根据这个客户端是否打开了REDIS_DIRTY_CAS标识来决定是否执行事务
     - 如果标记被打开，所以服务器拒绝执行客户端提交的事务
     - 如果标记未打开，服务器将执行这个事物
     
### 事务ACID性质
 * Atomicity原子性
   + 数据库将事务 的多个操作当作一个整体来执行，服务器要么就执行事务的所有操作，要么就一个也不执行
   + redis满足
   + redis不支持回滚，如果发现某个命令执行失败，则跳过这个命令继续执行下一条命令
      - 因为redis认为事务执行时错误通常都是编程错误产生的
   
 * Consistency一致性
   + 如果数据库在执行事务之前是一致的，那么在事务执行后，不管事务执行成功失败，数据库仍然是一致的
   + 一致是指数据符合数据库本身的定义和要求，没有包含非法或者无效的错误数据
   + redis通过错误检测和简单的设计来保证一致性
     - 入队错误：如果一个事务在入队命令过程中，出现错误命令，那么将拒绝这个执行这个事务
     - 执行错误：如果事务执行中某个命令出错，则跳过个命令
     - 服务器停机：
     
 * Isolation隔离性
   + redis事务部是以串行的方式运行的，并且事务部是具有隔离性
 * Durability持久性
   + 不同持久化方式与配置策略，对持久化支持不同
 
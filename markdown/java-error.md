## java问题排查

### 应用没响应
 * 出现原因
   + 资源耗光（cpu, 内存）
   + 死锁
   + 处理线程池耗光
  
 * 排查方法
   + 死锁
      - jstack -l
      - 仔细看线程堆栈信息
   + 处理线程池不够
      - jstack 查看从请求进来的路径上经过的处理线程池中的线程状态
      
 * 解决办法
   + 死锁
      - 解开锁想办法
   + 处理线程不够
      - 加线程或减少超时时间
      
### 调用另一应用超时
 * 出现原因
   + 服务端响应慢
   + 调用端或服务端GC频繁
   + 调用端或服务端CPU消耗严重
   + 反序列化失败
   + 网络问题
 * 排查
   + 查看服务端日志和响应监控信息
   + 查看gc log
   + 查看cpu利用率
   + 有没有反序列化失败
   + 网络的重传率

### OutOfMemoryError
 * 
 
### CPU us高（应用占用高）
 * 出现原因
   + CMS GC/ full GC频繁
   + 代码中出现非常耗时CPU操作
   
 * 排查
   + jstat -gcutil
   + top -H
   
 * 解决
   + 优化

### CPU sy高（系统占用高）
 * 出现原因
   + 锁竞争激烈
   + 线程主动切换频繁
 * 排查
   + jstack 看锁状况，是不是有主动纯种切换
 * 解决
   + 引入无锁数据结构
   + 线程主动切换：改为通知机制
   
### CPU iowait高
 * 出现原因
   + io读写操作频繁
 * 排查
   + iotop
   + blktrace + debugfs
   + btrace
 
 * 解决
   + 提升dirty page cache
   + cache
   + 同步写转异步写
   + 随机写转顺序写
   
### 进程退出
 * 出现原因
   + 很多
 * 排查方法
   + 查看出成的hs_err_pid.log
   + 确保core dump已打开, cat /proc/pid/limits
   + dmesg | grep -i kill
   + 
   
### 多线程问题
 * A线程
 ``` 
    // 共享缓存
    cache = new ConcurrentHashMap<>();
    condition = new ConcurrentHashMap<>();
    
    // 2. 执行
    func success(id): 
        condition[id] == true;
    
    
    func getObject(id):
        // 3. 执行
        Object A = cache.get(id);
        if(A == null) {
            Object B = db.get(id);
            
            // 6. 执行
            synchronized(cache) {
                if(!cache.containsKey(id)) {
                    cache.put(id, B);
                }
            }
            
            A = B;
        }
    
    return A;
 ```
 * B线程
 ``` 
   func clear(id):
        // 1. 执行， cpu被让出
        if condition[id] == false:
            // 4. 执行。 导致A被删除
            Object A = cache.remove(id);
 ```
 * 场景
   + 玩家A线程登录后的value1, 可能被B线程删除掉。因为B线程正在执行remove操作时，A线程登录的
   + 这种场景出现肯定是A线程的 if(o == null) 不满足，返回了A
   
 * 解决
   + 给id加版本
   + 整体接口加锁
   + 在3步对cache加锁，同时在1，4步对cache加锁
   + Object A 上设置一个状态，ok, removing, removed。原子更新
   + 在4执行完成后，再添加一次
   ``` 
        if condition == false:
            Object A = cache.remove(id);
            
            // 7. 执行。 会导致一个问题就是，如果A线程修改了3步拿到的A时, 又一次调用getObject(id)拿到了B。
            // 则7步的A不会被重新存，导致数据丢失
            synchronized(cache) {
                if(!cache.containsKey(id)) {
                    cache.put(id, A);
                }
            }
   ```
   
### fastjson序列化问题
 * 使用set方法参数实现序列化
 * 如果自己写的数据结构，再反序列化时，第三方框架只有默认jdk数据结构所以反序列化时可能会找不到自己定义的数据结构类型
   + List<Integer> list = new LinkedList<>()
   + setList(List<Integer> list). 默认在反序列化后使用的ArrayList<>(). 
 * 注意
   + 要实现默认构造函数
   + 注意get方法使用。默认将所有get方法对应属性序列化
   
   
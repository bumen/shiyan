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
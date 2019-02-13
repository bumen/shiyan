## NioEventLoop实现

### 性能考虑
 * 为了高效的利用CPU，EventLoop中只要有未消费的task则优先消费task。
 
#### run
 * 有任务就通过wakeup唤醒select()。或执行selectNow()
 * 先处理io事件
   + 先判断io连接是否有效，否则关闭连接
   + 再处理connection事件
   + 再处理write事件，尽快释放内存
   + 再处理read事件
   + 如果处理中发生异常则关闭连接
   + 同时会处理selectAgain。尽快释放无效连接
 * 再处理任务
   + 同时会处理selectAgain。尽快释放无效连接
 * 对关闭操作进行确认
 
### 对象创建
 * 创建Selector
   + 默认开启Selector优化功能。可以通过io.netty.noKeySetOptimization配置是否开启优化
   + 优化selectedKeys， publicSelectedKeys 使用相同自定义Set代替
   sun.nio.ch.SelectorImpl的实现中publicSelectedKeys = selectedKeys
   selectedKeys()方法返回的就是publicSelectedKeys。（即返回自定义Set）
   

### private final AtomicBoolean wakenUp = new AtomicBoolean() 作用
 * 目的：当有任务时控制唤醒selector.select(timeout)方法调用。防止阻塞执行。
 * 实现技巧：同时避免重复调用selector.wakeup()方法
   

### nio bug

#### cpu100%
 * SELECTOR_AUTO_REBUILD_THRESHOLD默认512。可以通过io.netty.selectorAutoRebuildThreshold配置
 * 检查bug
   + 在timeoutMillis时间到期前就返回了
   + 是Selector不管有无感兴趣的事件发生，select总是不阻塞就返回。
   + 这会导致select方法总是无效的被调用然后立即返回，依次不断的进行空轮询，导致CPU的利用率达到了100%
 * 通过判断空轮询次数SELECTOR_AUTO_REBUILD_THRESHOLD去重新创建selector
 
   
### select方法中timeout时间计算分析
``` 
 long currentTimeNanos = System.nanoTime();
 long selectDeadLineNanos = currentTimeNanos + delayNanos(currentTimeNanos);

 for (;;) {
    long timeoutMillis = (selectDeadLineNanos - currentTimeNanos + 500000L) / 1000000L;
    if (timeoutMillis <= 0) {
        if (selectCnt == 0) {
            selector.selectNow();
            selectCnt = 1;
        }
        break;
    }
 }
```
 * long scheduledTaskDelayNanos = delayNanos(currentTimeNanos); 
   + 用变量scheduledTaskDelayNanos表示延时执行时间
   + 返回最近一个待执行的定时/周期性任务延时执行时间
   + 如果没有定时任务，则返回1秒。
   + 如果有定时任务，返回定行任务延时执行时间，最小返回0（表示任务已经到达执行时间了）。
 * long timeoutMillis = (selectDeadLineNanos - currentTimeNanos + 500000L) / 1000000L;
   + 即timeoutMillis = (scheduledTaskDelayNanos + 0.5ms)/1ms;
   + 判断scheduledTaskDelayNanos 是否>=或< 0.5ms 
      - 如：scheduledTaskDelayNanos = 0.5, (0.5 + 0.5)/1 = 1, timeoutMillis=1
      - 如：scheduledTaskDelayNanos = 1.5, (1.5 + 0.5)/1 = 2, timeoutMillis=2
      - 如：scheduledTaskDelayNanos = 0.4, (0.4 + 0.5)/1 = 0, timeoutMillis=0
      - 特殊没有定时任务：scheduledTaskDelayNanos = 1000， (1000 + 0.5)/1 = 1000, timeoutMillis=1000
      - 特殊定时任务开始: scheduledTaskDelayNanos = 0, (0 + 0.5)/1 = 0, timeoutMillis=0
 * if (timeoutMillis <= 0) 
   + 表示scheduledTaskDelayNanos < 0.5ms 了。需要立即执行任务
 * 所以scheduledTaskDelayNanos有0.5ms的的误差
   + 表示定时任务已经到达执行时间或定时任务还没有到达执行时间，但还相差小于0.5ms时。本次就执行
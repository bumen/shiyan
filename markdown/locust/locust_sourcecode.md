## locust源码

### main.py

 * 总结 
    + 解析压测脚本，跟据不参数启动不同runner。 监听shutdown

#### main()
 * 启动locust服务
 * 解析启动参数
 * 配置日志级别
 * 查找压测脚本所在的绝对地址
   + 查找过滤看protoloader/bmn_main_test.py
 * 加载压测脚本模块，获取压测模块中所有Locust子类
 * 如果启动参数有-l, --list。则显示加载的所有Locust子类。并退出
 * 如果启动参数有--show-task-ratio，则显示Locust子类的权重比与每个子类中执行task权重比， 与总权重比。并退出
 * 如果启动参数配置-t， 则必须在--no-web（命令行模式）中使用。否则退出
   + 启动协程监听到时候后执行runner.quit()
 * 如果不是命令行模式且不是slive，则启动web
 * 创建runners
   + 如果不是master，slive，则创建LocalLocustRunner对象。如果是非web模式直接孵化且配置了执行时间则执行时间监听
   + 如果是master, 创建MasterLocustRunner对象。如果是非web模式，需要等所有slive都启动才启动成功开始孵化，并配置执行时间监听
      - 监听slave连接
   + 如果是slive， 创建SlaveLocustRunner对象，不能配置 -t参数。
      - 去连接master
   
 * 如果没有设置--only-summary， 且配置了--print-stats或者不是slave的命令行模式。则启动协程打印stats
 * 启动成功
 * shutdown
   + runner.quit()
   + quitting.fire()
   + 打印stats
   + 打印百分比
   + 打印错误
   + 退出
   
 
   
### inspectlocust.py
 * 配合main.py中main使用
 * 启动参数：--show-task-ratio
 * 根据Locust.weight, TaskSet.weight来计算Locust, TaskSet执行比
 * 只是打印提示信息。不参与正常辱测逻辑执行
 
 
### core.py
 * 定义Locust, TaskSet类
 
#### Locust
 * min_wait, max_wait, wait_function
   + 这三个属性如果TaskSet中未自定义，则默认使用locust这三个属性。
   + 作用：每次TaskSet执行完之后都会调用wait()->wait_function()->是在min_wait,max_wait之间随机。
   默认是1000s内随机，如果设置0，则不wait()
   
 * setup, teardown。
   + 从在初始化Locust时，如果有这两个方法，则不管创建多少Locust实例，只会执行一次。
   + teardown会被加入到quitting事件集合中等待被触发
   
 * stop_timeout: 控制TaskSet执行多久就停止
 * task_set: 设置Locust可以执行的TaskSet类
 * weight：配置Locust权重。产生蝗虫的比例
   + 我要生成6只Locust, 其中3只ALocust, 3只BLocust。则两只蝗虫配置一样的weight
   
 * 每个Locust只一个对应TaskSet对象，但TaskSet退出后，Locust退出
 * run方法
   + 会创建task_set(locust)对象
   + 执行task_set， run方法
   
   
#### TaskSet
 * 通过Locust执行来触发TaskSet的 run方法执行
 * min_wait, max_wait, wait_function
   + 如果不指定，则使用Locust中的属性
 * tasks： 所有要执行具体任务。@task中weight是多少就会创建多少个相同task放入tasks
 * setup， teardown
   + 从在初始化TaskSet时，如果有这两个方法，则不管创建多少Locust实例，只会执行一次。
   + teardown会被加入到quitting事件集合中等待被触发
   
 * run
   + 如果on_start方法，则先执行
   + while (True) : 死循环方法
   + 如果Locust配置了stop_timeout，则到时间后停止
   + 如果_task_queue中没有要执行的task, 则从tasks中随机一个放入队列 
   + 从队列中取出第一个task, 并执行
   + 执行完，调用wait(). 等待一段时间。再继续执行，直到退出
   
### runners.py
 * locust_runner状态
   + STATE_INIT ： 初始化状态
   + STATE_HATCHING：孵化蝗虫中状态，即创建Locust
   + STATE_RUNNING：运行状态
   + STATE_CLEANUP：清除状态
   + STATE_STOPPED：停止状态
   
 * LocustRunner
   + 有三种协程
      - hatching_greenlet：孵化协程
      - greenlet：runner主协程
      - locusts：蝗虫协程组：控制所有蝗虫的生死
      
   + stop
      - 删除hatching_greenlet与locusts协程
      - 还可以通过重新start_hatching启动
   + quit
     + 删除hatching_greenlet， greenlet，locusts
     + 程序退出
   + start_hatching：启动孵化
     + 如果当前是：STATE_INIT，STATE_CLEANUP，STATE_STOPPED状态，则要清除所有stats
     + 如果当前是：STATE_HATCHING，STATE_RUNNING，STATE_CLEANUP状态，则进入STATE_HATCHING状态，表示服务已经启动需要再动态添加一些蝗虫
     + 如果当前是：STATE_INIT，STATE_STOPPED状态，则首次启动孵化
     + 调用spawn_locusts
     
   + weight_locusts：要孵化多少只
     + 根据一共要创建多少蝗虫与不同蝗虫的比例来创建蝗虫。
     + 蝗虫比例都Locust->weight属性控制
     + 如：一个需要10只蝗虫，有两种蝗虫他们的weight都是10， 则ALocust创建5只，BLocust创建5只
     + 返回创建的所有蝗虫bucket
     
   + spawn_locusts：孵化过程
     + 先调用weight_locusts创建出需要孵化的所有蝗虫
     + hatch方法是一个死循环。将会不段的孵化出每一只蝗虫并创建一个协程去执行。当孵化完所有蝗虫后退出死循环
        - sleep_time = 1.0 / self.hatch_rate：孵化速率
        - 从bucket中随机一只去执行，然后跟据孵化速率不断孵化下一只，直到bucket为空
     
   
 * LocalLocustRunner
   + 有web模式
   + 命令行模式
   + hatching_greenlet，greenlet是同一个普通协程
   + 孵化蝗虫，web模式由web控制开启孵化，命令行模式在启动时进行孵化
 * MasterLocustRunner
   + 有web模式
   + 命令行模式
   + stop
     - 通知所有slive去stop
   + quit
     - 通知所有slave去quit
     - master退出
     
   + greenlet是一个socket监听协程组；没有hatching_greenlet
   + 孵化蝗虫，web模式由web控制开启孵化
   + 孵化蝗虫，命令行模式在启动时进行孵化
      - 需要先判断至少一个slave准备好，才会开启孵化
      - 将孵化蝗虫数与孵化率平均分给不同slave, 发消息通知他们开启孵化
   
 * SlaveLocustRunner
   + 没有web模式
   + greenlet是一个socket, report协程组；hatching_greenlet是普通协程
   + 先要启动master
   + 每启动一个slave后都会通知master我已经准备好。
   + 接收master: hatch, stop, quit消息
   + 发送master: client_ready, client_stopped, stats, hatching, hatch_complete, quit, exception消息
   + 孵化蝗虫
   
   + 每隔3秒上报数据
   
### events.py
 * request_success
   + client执行请求后，获取响应成功时触发此事件。用于统计接口测试结果
   
 * request_failure
   + client执行请求后，获取响应失败时触发此事件。用于统计接口测试失败
   
 * locust_error
   + 当TaskSet执行失败时：抛出未知异常会执行：events.locust_error.fire(locust_instance=self, exception=e, tb=sys.exc_info()[2])
   + 由runners负责统计。如果是slave负责将异常通知到master
   
 * report_to_master
   + slave每隔3秒将统计数据时触发，然后发送给master
   
 * slave_report
   + master接收到slave消息类型是stats时（即统计消息），触发
   + master负责汇总统计数据
   
 * hatch_complete
   + 孵化完成时触发
 * quitting
   + main的shutdown时调用：events.quitting.fire(reverse=True)
   + Locust创建时如果有teardown方法，会加入到quitting中
   + TaskSet创建时如果有teardown方法，会加入到quitting中
 * master_start_hatching
 * master_stop_hatching
 * locust_start_hatching
 * locust_stop_hatching
 
### stats.py 统计
 
#### global_stats对象
 * 向外暴露：global_stats对象
 * global_stats=RequestStats
 
#### RequestStats
 * 是对StatsEntry统计调用接口
 * 属性
   + self.entries： List<StatsEntry>
   + self.errors: List<StatsError>
   + self.total: StatsEntry, StatsEntry.use_response_times_cache=true
   + self.start_time
   
 * 提供接口
   + log_request
   + log_error
   + reset_all
   + clear_all
   + serialize_stats
   + serialize_errors
   
 
   
#### StatsEntry
 * 属性
   + name：一般为url的path
   + method: get, post
   + num_requests: 有响应的请求总数：包括成功的请求和失败的请求
   + num_failures：响应失败的请求数
     - 什么情况下算失败
   + total_response_time：总响应时间
   + min_response_time: 最小响应时间
   + max_response_time：最大响应时间
   + num_reqs_per_sec：dict{timestamp=>request_count}, 1秒内有多少响应返回
   + response_times：dict{response_time=>count}, 相同响应时间有少请求
   + response_times_cache：是一个OrderedDict，存放最近20s的内response_times，num_requests 快照
   + total_content_length：响应的总消息长度
   + start_time：收到的第一个响应时间
   + last_request_timestamp：收到的最后一个响应时间
   
 * log
   + 每次收到响应时调用
   + use_response_times_cache=true, 最快情况是每秒记录一次
   + avg_response_time：平均响应时间= total_response_time/num_requests
   + median_response_time：返回当前中间响应时间，num_requests中一半的请求响应时间小于某一值，一半大于这个值
   + current_rps：返回当前平均响应数：计算最近10秒内的平均值。如果10秒内只有 3，6秒各返回5个，10请求。则 reqs=[5,10], avg=5+10/len(reqs)=2
     - 有一个TotalState计算rps, 即所有方法的
     - 有一个不同方法的State计算rps, 只计算该方法的rps
     > 因为一个locust只能同时执行一个测试方法。如共有10个locust, 则10个locust则行相同task, 且一秒返回，则rps = 10/1 = 10
     > 所以测试一个接口的rps, 就创建一个task, 只要相同间隔调用一个task就可以。保证同时间返回。达到准确rps
     > 但是少量locust不好做到这一点。还是需要启更多的locust, 这样随机到相同时间执行同一个task机率更高，rps更准确
     > 不用同时启动locust, 因为计算的是最近10秒的返回。还是多locust更准确。所以还是使用slave方式更准确
     
   + total_rps：返回当前总平均响应数：总请求成功数/（第一个响应时间到最后一个响应时间）的秒数|
   + avg_content_length： self.total_content_length / self.num_requests
   + get_response_time_percentile：获取不同比例的请求的响应时间。响应时间为ms
     - 如：共发送了100个请求，则调用接口
     `get_response_time_percentile(0.5)`
     - 即计算一下百分之50的请求的，响应时间，即前100*0.5 = 50个请求的响应时间
     `get_response_time_percentile(0.6)`
     - 即计算一下百分之60的请求的，响应时间，即前100*0.6 = 60个请求的响应时间
      
   + get_current_response_time_percentile: 获取最近20s内的不同比例的请求的响应时间
   
   
 * 响应时间统计
 ``` 
    // 响应时间单位ms
    // 为了避免创建过多响应数据。对响应时间四入五入
    // 小于100ms。则直接统计
    // 100 <= x < 1000ms 则对尾数四舍五入。如：147-> 150, 149-> 150, 142-> 140。精确到十毫秒
    // 1000 <= x < 10000ms(10秒），则对尾2位四舍五入
    // 10000 <= x，则对后3位四舍五入。
    if response_time < 100:
        rounded_response_time = response_time
    elif response_time < 1000:
        rounded_response_time = int(round(response_time, -1))
    elif response_time < 10000:
        rounded_response_time = int(round(response_time, -2))
    else:
        rounded_response_time = int(round(response_time, -3))
 ```
 
#### 命令行模式
 * print_stats
   + console中打印当前状态
 * print_percentile_stats
   + console中打印不同响应时间的请求比
 * print_error_report
   + console中打死错误
 * stats_printer
   + 每隔2秒打印
 * stats_writer
   + 写入cvs文件
 
#### 事件触发
 * request_success
   + 记录当前响应
 * request_failure
   + 记录当前失败
 * report_to_master
   + slave上报到master
 * slave_report
   + master接收slave上报数据
 

   
 
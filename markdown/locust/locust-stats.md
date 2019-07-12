## 统计


### 数据记录
 * 记录哪些数据，然后跟据数据去统计
 * 什么时候记录
   + 请求响应后记录
   
 * 记录类型
   + 总的所有接口数据记录
   + 每个不同接口数据记录
   
 * 记录什么
   + 接口标识：如name, method
   + 接口第1次调用时间（成功+失败）：start_time
   + 接口最后1次调用时间（成功+失败）：last_request_timestamp
   + 接口调用总次数（成功+失败）：num_requests
   + 相同请求时间的请求数（成功+失败）：num_reqs_per_sec<请求时间戳(秒), 次数>
   + 相同响应时间的请求数（成功+失败）：response_times<响应时间(毫秒), 次数>
   + 响应数据总大小：total_content_length
   + 响应时间总大小：total_response_time
   + 最小响应时间：min_response_time
   + 最大响应时间：max_response_time
   + 接口失败次数：num_failures


### 数据统计 
 * 实时统计
 * 最终统计
 
 * 统计什么
   + 总的失败率：  
   `num_failures/num_requests`
   + 平均响应时间  
   `total_response_time/num_requests`
   
   + 中间响应时间：总请求次数中，一半的请求响应时间小于这个值，另一半大于这个值
      - 中间值：mid = len(num_requests)/2
   `从response_times中查找mid`
   
   + 当前rps。计算当前服务器实时rps。（可能服务器压力越来越大，导致rps降低）
      - 可以观察服务器运行一段时间后，处理能力
      - 统计从last_request_timestamp-12开始的请求。到last_request_timestamp-2结果的请求
      - 即统计最近10秒内的请求的rps。所单机测试的话这个值很小。导致rps也不大
      - 取num_reqs_per_sec中最近10的所有请求次数reqs = [10,20,30], reqs1 = [20, 40]
      - rps = sum(reqs) / len(reqs) = 60 / 3 = 20。 rps1 = 60 / 2 = 30
      - 表示：这个接口60次请求最近处理能力是20。对比rps1更高，所以处理更快
      
   + 总rps  
   `num_requests/(last_request_timestamp-start_time)`
   
   + 平均响应消息大小  
   `total_content_length/num_requests`
   
   + 响应时间百分比
      - 获取num_requests中不同百分比的请求的响应时间是多少
      - num_requests = 100
      - pn1 = 100 * 50% = 50。即50个请求的最大响应时间
      - pn2 = 100* 80% = 80。 即80个请求的最大响应时间
      - pn3 = 100 * 95% = 95。即95个请求的最大响应时间
      - 从response_times中查找
      
   + 当前响应时间百分比： 这个是实时统计。
      - 观察服务器运行一段时间，的响应时间
      - 统计最近20秒内的请求，的响应时间百分比
      - 用于统计总的接口数据
      - 记录时，需要缓存最近20秒的 response_times, num_requests的快照
      - 统计时，用当前的状态-缓存中的状态。now:respsonse_times - old:response_times
        now:num_requests - old:num_requests
      - 然后统计差值的response_times，num_requests 不同百分比最大响应时间
      - 与统计当前rps比较。因为rps有时间戳，所以不需要缓存
    
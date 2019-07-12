## Http client实现


### 主要request方法
 * 接口参数
   + method: 请求方式
   + url: 请求地址
   + name: 请求路径
      - 用于request_meta记录。
      - 如果不传，则使用url, path属性
   + catch_response 默认false
      - 当catch_response=true时，会返回一个正常response代理对象
      - 目的：调用方再拿到response后，可以自已控制记录这次请求成功还是失败（需要手动记录）
      - 默认自动触发数据记录
      
 * request_meta
   + 从几个维度记录了一个请求过程中的信息
   + method：请求方式，接口调用时指定
   + start_time：请求开始时间，用于计算响应时间
   + response_time：请求响应时间，now - start_time
   + name: 请求名称。区分不同的请求
   + content_size： 响应大小
   
### 请求过程异常处理
 * 当发起http请求时处理
   + 对于发起io传输前如果抛出异常。则直接向上抛出异常交给调用方处理
      - 如url格式不正确。schema错误
      
   + 对于io传输中的异常。则返回一个response对象。设置失败对象
      - 正常返回
      
 * 收到正常response后的消息异常处理
   + 100-300的response code都是正常response
      - 真正的正常返回。触发request_success事件
      
   + 其它的response code 都是非正常response. 如：404
      - 触发request_failure事件
      

   
   
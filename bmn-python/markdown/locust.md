## locust 压力测试

### 安装


#### 依赖
 * gevent : 实现协程的第三方库，协程又叫微线程（Corouine）.可以获得级高的并发性
 * flask：一个web框架
 * requests：支持http/https访问库
 * msgpack-python：一种快速、紧凑的二近制序列化格式，使用与类似json的数据
 * six：提供了一些简单的工具封装Python2与python3之间的差异
 * pyzmq：可以把locust运行在多个进程或多个机器（分布式执行测试任务）
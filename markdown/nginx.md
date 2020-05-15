## nginx

### 功能
 * 反向代理
   + proxy_pass 将请求转发到有处理能力的端上，默认不会转发请求中的 Host 头部
 * 负载均衡
   + upstream
   
 * 动静分离

### nginx 配置
``` 
main
events{}
http{
  server{
    location / {
    
    }
  }
  
  upstream xx {
  
  }
}

```
#### main模块
 * user nobody nobody  //指定nginx worker进程运行用户级别以及用户组
 * worker_processes 2 //指定nginx开启的子进程数
   + 每个进程平均耗费10m-12m内存。
   + 根据经验一般一个进程就足够了，如果是多核，则指定与cpu数量一致
   + 如果写2，则是2个子进程，一共个3个进程
 * error_log /data/log/err.log debug
   + 全局错误日志，debug, info, notice, warn, error, crit
 * pid
 * worker_rlimit_nofile 1024 //指定一个nginx进程可以打开的最多文件描述符数量。
 
#### events模块
 * use kqueue
   + 指定工作模式，支持有select, poll, kqueue, epoll, rtsig, /dev/poll
   + select, poll是标准模式
   + kqueu(BSD系统), epoll(linux系统)是高效模式
 * worker_connections 1024
   + 每个进程最大连接数
   + 最大客户端连接数= worker_processes * worker_connections
   + 作为反向代理=worker_processes * worker_connections/4
   + 进程最大连接数受linux进程最大打开文件数限制
   
#### http模块
 
#### server模块
 * 是http的子模块，可以有多个
 * 指定虚拟主机
 
#### location模块
 * 用来定位url, 解析url
 * location~来实现正则匹配
 * root属性，定位web资源 
 
#### upstream模块
 * 负责负载均衡
 * weight轮询（默认）
 * ip_hash
 * fair
 * url_hash
 * 服务器状态
   + down：关服
   + backup: 备用，当非backup机器出现故障或忙时使用
   + max_fails：允许请求失败次数，默认1，当大于1时，返回proxy_next_upstream模块定义的错误 
   + fail_timeout：经历了max_fails次数后，暂停服务时间
   
 
#### nginx静态代理



#### nginx反向代理
 * 什么是正向代理
   + 代理的是客户端，代理服务也相当于一个客户端，去访问服务器
   + 真实客户端不能直接访问服务器，只能通过代理服务去访问
   + 一些真实客户端需要配置请求的代理服务，才能访问代理服务
 
 * 什么是反向代理
   + 代理的是服务器，代理服务也相当于一个服务器，接收客户端请求
   + 客户端只要访问代理服务器就可以，不知道真正处理的服务器是谁
  
### 使用
 * nginx 适合静态请求，与反向代理。并不适合动态请求
 * apache 适合动态请求，与后端 apache 集群
## redis安装

### Mac安装
 * 下载redis-5.0.4.tar.gz
 * 解压redis-5.0.4.tar.gz到redis-5.0.4
 * 进入解压目录
 * make
 * make install PREFIX=自定义的安装目录
 * 配置命令软链接
 * 安装完成
 
#### 启动
 * 创建启动目录
   + /data/home/user00/bmn/redis_work/6379/
 * 从解压目录redis-5.0.4拷贝redis.conf
 * 修改配置
   + daemonize yes
   + logfile "/Users/xxx/data/home/user00/log/redis/redis_6379.log"
   + dir /Users/xxx/data/home/user00/bmn/redis_work/6379/ 
   + pidfile /Users/xxx/data/home/user00/pidfile/redis_6379.pid
 * 启动命令
   + redis-server /data/home/user00/bmn/redis_work/6379/redis.conf
 
#### 停止 
 * redis-cli -p 6379 shutdown
 
 
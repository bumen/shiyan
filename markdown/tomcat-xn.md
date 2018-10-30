## tomcat性能优化

### 内存使用配置
 * tomcat依赖JVM，所以tomcat内存使用配置实质上JVM内存配置
 * 通过/bin/catalina.bat修改
 
### Connector 协议
 * 使用nio
 * 加入属性
   + maxThreads: 当前可以同时处理的最大用户数
   + minSpareThreads： 最小空闲线程连接数，用于优化线程池
   + maxSpareThreads：
   + acceptCount：当所有线程以分配，仍然允许连接进来，但是出于等待状态的用户数
   等待线程数+工作线程数=总的最大连接数，如果超过此数，则新连接不会被接受
   + enableLookups="false"：是否允许
 
### Linux命令
 * ps -o nlwp [pid]
   + 查看进程下有多少线程，包括idel状态
 * ps -eLo pid,stat | grep [pid] | grep running | wc -l
   + 查看进程下有多少running线程
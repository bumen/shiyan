## TCP相关

### 单机最大TCP连接数

### 构造虚拟TCP请求连接客户端

### 构造虚拟TCP响应连接服务器

### linux文件句柄
 * 1G内存的机器上大约有10万个句柄左右
 
### 状态转换
 
### 问题
 * 大量CLOSE_WAIT
   + 由于程序错误，没有关闭出现异常的连接
   + 通过配置参数缩短超时时间
 
 * 大量LAST_ACK
   + 由于主动方ACK包丢失
     - 重发fin
   + 由于主动方CLOSE
     - 此时直接LAST_ACK重传超时，进入CLOSE
   + 由于主动方商品被其它连接占用
     - 此LAST_ACK会再发送FIN关闭，此时主动会返回RST.最后LAST_ACK进入CLOSE
 
 * 大量TIME_WAIT
   + 服务端尽量不要主动关闭
   + 通过配置缩短TIME_WAIT时间
 
 * 大量FIN_WAIT_2
   + 通过配置超时，时间
## TCP 连接错误


### Connection reset
 * 服务器返回REST时，client在往socket的输出流中读取数据会提示“connection reset”
 + 此时应该关闭连接
### Connection reset by peer
 * 服务器返回REST时，client在往socket的输入流中写数据会提示“Connection reset by peer”
 + 此时应该关闭连接
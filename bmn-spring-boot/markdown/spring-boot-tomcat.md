## tomcat调优

### connectionTimeout
 * 连接读写超时时间，默认60s, 
 * 可以通过server.connectionTimeout 自行自定义配置
 
### keepAliveTimeout
 * 空闲连接存活时间
 * 不能通过属性配置，默认如果为null, 使用connectionTimeout值。所以也默认是60s
 
### maxKeepAliveRequests
 * 一个连接最大处理请求数，默认100
 * 避免重复创建连接带来的消耗，可以调大
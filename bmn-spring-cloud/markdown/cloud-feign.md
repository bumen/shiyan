## feign 声明式服务调用

### feign描述
 * feign整合了 ribbon和 hystrix, 除了整合这两者强大功能外，还提供了声明式服务调用
 * Feign是一种声明式，模板化的http客户端，在spring cloud中使用Feign, 我们可以做到使用http请求远程服务时能与调用
 本地方法一样的编码体验，开发者完全感知不到这是远程方法，更感知不到这是个http请求。
 
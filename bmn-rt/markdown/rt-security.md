## Java可扩展安全架构
 * https://blog.csdn.net/allenwells/article/details/46473261
 * java平台使用基于标准的安全API技术提供可扩展的安全架构模型
 * java加密架构(java cryptograp architecture)
   + 提供基本的加密服务和加密算法，包括对数字签名和消息摘要的支持。
 * java加密扩展(java cryptographic extension)
   + 采用遵循美国出口控制条例的加密服务来增强JCA功能，同时支持加密、解密操作，支持密钥生成和协商以及支持消息验证码算法(Message Authentication Code)
 * java证书路径(java Certification path) 
   + 提供检查、验证、和确认证书链真实性的功能
 * java安全套接字扩展(java secure socket extension)
   + 通过使用SSL/TLC协议来保护数据交换的完整性和机密性
 * java认证和授权服务(java authentication and authorization service)
   + 验证用户或设备的身份以确定其真实性和可信度，然后根据其身份提供访问权限，它有助于采用可插入的认证机制和基于用户的授权
 * java通用安全服务(java generic secure)
   + 提供了使用统一的API开发应用功能，应用可以支持各种认证机制并有利于单点登录
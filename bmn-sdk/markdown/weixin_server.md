## 微信开发认证过程
 * 前端jsapi

### 申请测试公众号
 * 简单
### 服务器端拉取Access_token
 * 参数AppId
 * 参数AppSecret
 
### 服务器端拉取ticket
 * 参数
   + Access_token
   
### 服务器端保存, 必免每次去微信端拉取
 * Access_token
 * ticket 
 


 
### 前端JSAPI 认证

1. 去服务器获取ticket

2. 根据url ticket 进行签名
 * 签名，可以直接要前端使用ticket，通过sha1签名
 * wx.config()
 
3. 使用接口

 

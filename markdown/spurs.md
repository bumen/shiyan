## playcarb spurs项目

### 目录
 * vms系统，客户端升级系统
 * global系统，客户端登录系统
 * spurs服务器，游戏服务器
 
### vms系统
 * 如果有升级，则升级完成后还需要再走一次vms系统，如果这些没有升级则请求global系统
 * 客户端灰度版本：当客户端当前版本> 线上正式版本时
   + 灰度版本，需要回退出线上正式版本
   + 
 * php实现，简单，脚本语言更新配置直接生效，http服务
 * 流程：
  1. 客户端上传不前版本号 ver
  1.1 判断服务器是否为全区停服维护状态
    + 如果是，则需要判断客户端请求参数， 如果参数与服务器匹配说明是测试客户端，则允许进入
    + 如果是，则需要判断白名单，如果客户端ip是白名单，则允许进入
    + 否则出错，不允许进入
  1.2 判断客户端是否为ios审核版本
    + 如果是，则不需要升级，直接返回config数据，path, gameStatic信息
    
  2. 跟据ver，去ver版本目录查找info.php获取update_path(升级续列)
  3. 跟据update_path获取当前后端配置的最新线上客户端版本通过online_version.php获取最新version
  4. 跟据version，去version版本目录查找info.php
  5. 跟据ver, version
    + 如果ver >= version则说明已经是最新了，不需要升级
    + 否则获取version的update.php文件，从update.php文件获取ver版本要升级到version版本所需要的文件
  6. 获取config
    + 升级资源地址
    + 如果前端指定要升级的版本，则返回灰度服地址
    + 如果前端未指定要升级的版本，则返回global地址
  7. 跟据version获取path(当前客户端的补丁文件)
    + 通过把version对应的info.php中的配置信息与客户端请求参数作为path过滤条件数据
    + 查询config目录下的Path.php中的配置跟据条件如果满足，则返回相应path
  8. 跟据version获取GameStatic， 即客户端一些功能开关控制机制（如：开启debug模式）
    + 通过把version对应的info.php中的配置信息与客户端请求参数作为path过滤条件数据
    + 查询config目录下的GameStatic.php中的配置
    + 跟据except属性中的条件过滤出不同值，如果没有except则使用默认值。
 * 最终返回
   + 线上最新版本
   + 是否需要升级
   + 升级资源
   + 升级地址
   + global地址
   + path
   + GameStatic
   
### global系统
 * 获取区服
 * 转发gm请求，到游戏服
 * sdk登录
#### sdk登录过程
 * 跟据platform属性选择不同的sdk登录
 * 登录成功后，返回给客户端登录key
 * 客户端携带key，去登录游戏服
 * 游戏服通过验证key，来过滤客户端登录请求
 
#### gm转发请求
 * 通过验证gm白名单，（以前是对数据跟据双方密钥做md5，签名验证）
 
#### 获取区服列表
 * 跟据platfrom不同，返回不同的区服
### 安装

#### windows 安装 4.2
 * 下载community msi
 * 走安装引导
   + 自定义安装
   + 选择安装目录
   + Install Mongod as a Service
     - 选择Run service as Network Service user
     - 这一步是安装windows 服务，名称为MongoDB
     
   + 跳过安装compass
   + 完成
   
 * 引导完成后，会自己启动MongoDB服务
   + 数据与日志都在mongodb命令所在目录
   + 默认配置使用的bin\mongo.cfg
   
 > 以上mongodb安装完成，如果要自定义数据日志，mongodb配置等需要删除原来mongodb服务，自己新创建一个服务
   
 * 启动命令行，已管理员身份
   + net stop MongoDB
   + sc delete MongoDB
      - 把安装的mongodb 服务删除
 
   + 自定义mongodb 工作目录 
     - /data/home/mongo_work/27017
     - 将mongo.cfg配置拷贝到工作目录
     - 创建data目录
     
   + 安装服务
     - mongod -f /data/home/mongo_work/27017/mongo.cfg --install --serviceName MongoDB
     
   + 启动
     - net start MongoDB
     
 * 完成
 
 * 创建mongodb用户
   + 需要在安装服务前先使用命令行启动服务
     - mongod -f /data/home/mongo_work/27017/mongo.cfg
     - 使用mongo命令登录
     - 创建用户
     - 关闭mongod服务
     - 创建mongo服务，注意此时开启权限校验配置。
  
   
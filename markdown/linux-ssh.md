## ssh登录

### windows配置.ssh
 * 添加config文件
 * 配置端口转发
 ``` 
    Host stagingC
    HostName 11.8.2.2 // 远程要登录的服务器
    Port 22
    User sound        // 登录用户名
    ServerAliveInterval 10    // 10s发一次心跳防止掉线
    IdentityFile ~/.ssh/id_rsa  // 配置免密登录
    LocalForward 7001 ww.baid.com:2221    // 本地端口7001， 转发到跳版机2221端口
 ```
 
 ### 在本地添加ssh-key到远程服务器
  ``` 
    ssh 服务器用户名@服务器地址 "echo \"`cat .ssh/id_rsa.pub`\" >> .ssh/authorized_keys"
  ```
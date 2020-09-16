### mysql安装 











### mysql 8.0.20.x64 windows解压安装
 * 解压到指定目录
 * 自定义一个basedir目录，即mysql数据目录，mysql启动后都在这个目录的相对目录
 * mysqld --initialize --console --basedir=
   + 初始化mysql，会生成一个默认root的账号与密码
   + 这个账号用于首次登录mysql使用
   
 * mysqld --defaults-file=[自定义配置文件]
   + 启动mysql
   + 此时命令行启动
 * mysql -uroot -p 密码
   + 首次进入Mysql
   + 首次进入需要修改mysql root账号密码  
   ` ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '你的密码';  `
   
 * mysqld --install MySQL8.0 --defaults-file="K:\mysql\mysql8.0\my.ini"
   + 使用服务启动
   + net start MySQL8.0
     


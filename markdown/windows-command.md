查看80端口是否被占用，命令行下输入：
netstat -aon|findstr "80"

在命令行界面中打开另一个窗口
start cmd

在命令行打开管理员窗口
runas /user:baw cmd

列表---dir

输入该命令后查看服务是否安装成功
services.msc


start .  打开当前文件夹


set/p option=SheetNames:   
        /p 是等用户输入 ，把输入的值给变量option

set var=%~dp0   ---设置当前文件所在目录给var变量

cd /d %var% ---直接切换到var目录

cd /d %~dp0的意思就是cd /d d:\qq
%0代表批处理本身 d:\qq\a.bat
~dp是变量扩充
d既是扩充到分区号 d:
p就是扩充到路径 \qq
dp就是扩充到分区号路径 d:\qq


tasklist ---查看 服务的pid
tasklist | find "java"  过滤

tasklist /fi "PID eq 9552"

taskkill /pid 2552 -f   强制删除一个进程
   ntsd -c q -p 2724  关闭进程

net start mysql 
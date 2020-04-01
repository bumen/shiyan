### idea-2019.1-2019.3.1破解
 * 安装
 * 打开idea
 * 进入安装目录/bin 
   + 修改clion64.exe.vmoptions文件
   + 添加：-javaagent:D:\Program Files\JetBrains\CLion 2019.3.1\bin\jetbrains-agent.jar
   > 注意：路径不要有中文
   
 * 菜单
   + help->edit_custom_vm_options->yes, （这一部可以省略）
   + help->register->licenseserver
     - http://jetbrains-license-server
     - activate
  
  
  
### windows clion安装配置
 
#### 配置build->toochains
 * Cygwin
   + 下载https://cygwin.com/setup-x86_64.exe
   + 安装
   > install from Internet -> 设置下载安装路径 -> direct connection -> 选择下载源（添加网易镜像站：http://mirrors.163.com/cygwin/）
   > 接来下选择安装的模块，分别搜索 gcc-core、gcc-g++、make、gdb、binutils，以上所有项目都在 devel 文件夹下
   > 有个search的搜索框，搜上面几个出来结果后点一下前面那个skip，把skip切换成版本号，意思是原来skip（跳过）的模块现在安装。 
   + idea配置到cygwin安装目录
### [Why GPM is the Right Go Package Manager](http://technosophos.com/2014/05/29/why-gpm-is-the-right-go-package-manager.html)
 * 为什么要使用GPM来管理go依赖包，而不是其它的

#### 主要内容
 * 包管理工具的主要作用是可以实现依赖包的版本管理
 * 没有版本的依赖包问题
   + 不能确保不同人编译是否一致
   
 * 普通管理方式 
   + 依赖包的head版本，但如果head版本修改，则没有通知使用者，那么可能对使用者不友好
   + 在module下创建一个vendor, 将依赖包都放到vendor下。则使用的时候引入包path都需要加上vendor
   
 
 * GOPATH
   + 通常所有项目使用一个GOPATH，不能满足不同项目使用不同GOPATH
 
#### GPM （Go Package Manager）
 * module下需要一个Godeps配置文件，指定引入的包
 * 执行gpm install 后，自动安装包到GOPATH下
 * 好处
   + 有了依赖包的版本管理
 * 问题
   + 不能实现不同项目使用不同GOPATH
   
#### GVP （Go Versioning Packager）
 * 管理GOPATH工具
 * 可以实现不同项目使用不同GOPATH
 * 会在module目录下创建一个.godeps目录
   + 这个目录会被加入到GOPATH中
   + 可以随机删除重建
   
 * 创建使用
   + 进入module目录 
   + gvp init： 初始化目录
   + source gvp in： 设置GOPATH环境变量
   
   + gpm install 安装包
   + go run 
   
 * 还原使用
   + source gvp out 
   
 * 如果不使用GVP，则手动模拟
   + cd module 目录
   + cat .env (随意名称，git中的gpm.gif中使用的是env)
   + export GOPATH="$PWD"/.dependencies:"$PWD"
   + source .env 
   
   + gpm 
   + tree -L 4 .dependencies 
     + 安装到此目录中
     
 * 安装
   + 去git上，tags标签下载zip 解压
   + 获取bin目录下的gvp
   + windows
      - 将gvp放到git/usr/bin目录
   
#### 安装
 * 去git上，tags标签里下载zip 解压
 * 获取bin目录下的gpm
 * windows 
   + 将gpm放git/usr/bin目录
   
   
#### GPM-git
 * 扩展了GPM, 可以为包启别名，通过别名引用
 * 安装
   + 去git上，tags标签下载zip 解压
   + 获取bin目录下的gpm-git
   + windows
      - 将gvp放到git/usr/bin目录
   
   
#### idea 中使用
 * 创建一个项目
   + 使用gvp生成项目GOPATH目录 即.godeps
   + 使用gpm-git 下载依赖
   + 打开idea 添加一个module go path 指定到当前项目的go path
      - 现在就可以在idea中实现代码跳转了与运行了

### go项目
 
 
#### [How to Write Go Code](https://golang.org/doc/code.html#GOPATH)
 * 官方包管理
 * go程序都是组织成包的形式
 * 一个module包括一组一起发布的相关的包
 * 一个仓库包括一个或多个module
   + 通常是一个module，作为仓库的根目录
   
 * go.mod
   + 定义了module path
   
 * module path: 
   1. 是一个module下所有包的引用前缀
   2. 可以是go command 的下载包的地址
   
 * import path
   + 引包时使用
   + 等于：module path + 包的子目录
   
 * 一个module包括的内容
   + 一个go.md，以及这个目录下的所有包，以及其子目录下的包
   + 一直到另一个包括go.md的包为止
   
   
 * 使用
   + 在编译和运行一个go程序时，首先指定一个module path 并创建一个go.mod
   + 执行：go install [module path]
   + install 目录 通过GOPATH和GOBIN指定 
      - 如果GOBIN指定了，则会安装到GOBIN目录
      - 如果GOPATH指定了，则会安装到GOPATH指定的第一个目录的bin子目录中
      - 否则安装到GOPATH默认的bin子目录
     > 所以需要设置GOPATH目录，来指定安装目录    
   
      
   > go install 需要在module目录下执行，否则会出错
   > go install . 或 go install 简写，默认指定到当前目录
 
   + 引入远程包
     + 通过import path 引入
     + 当执行go install, go build, go run 时， go会下载这些包
       - **下载同时会将版本信息写入go.mod文件**
       - 下载到GOPATH指定目录的pkg/mod子目录中，这些包会被使用相同version不同module共享使用
          - 别的module也想使用这个包时，在go.mod文件是引入即可
          ``` 
            require github.com/google/go-cmp v0.4.0
            
            //or 
            require (
                github.com/armon/go-socks5 v0.0.0-20160902184237-e75332964ef5
                github.com/coreos/go-oidc v2.2.1+incompatible
            )
          ```
       - go clean --modcache 清除命令
       
       
       
      
 * 设置GO env 变量
   + 设置变量：go env -w GOBIN=/xx/xx
   + 删除变量：go env -u GOBIN

    
 * go test 框架使用 
   + 文件名：xxx_test.go 以_test.go结尾
   + 方法名：TestXXX
   + 方法签名：TestXXX(t *testing.T)
   + 返回失败
     - t.Error or t.Fail
     
   + 代码
     
   ``` 
   package morestrings
   
   import "testing"
   
   func TestReverseRunes(t *testing.T) {
        cases := []struct {
            in, want string
        }{
            {"Hello, world", "dlrow ,olleH"},
            {"Hello, 世界", "界世 ,olleH"},
            {"", ""},
        }
        for _, c := range cases {
            got := ReverseRunes(c.in)
            if got != c.want {
                t.Errorf("ReverseRunes(%q) == %q, want %q", c.in, got, c.want)
            }
        }
       }
    
   ```
   
### go build 
 * 不会生成输出文件，而是将编译的包保存到build cache中
 
 
 
 

   
 
   
   
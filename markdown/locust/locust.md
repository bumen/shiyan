## Locust
 * http://debugtalk.com/post/head-first-locust-user-guide/
 * https://www.missshi.cn/#/seriesBlog?_k=t2i5om

### 源码安装
 * python setup.py install
 * 会将locust.exe安装到python home/scripts目录下
   + 所以也要将这个目录放到path中
   
### 命令行参数
 * -h: 查看帮助
 * -H: 被测服务器的域名。
   + 如果想启动的时候，不加“-H”参数，那么在启动脚本里面的就要加上 host="http://sample"，写在HttpLocust子类里面。
   脚本里面写 get或post请求 的时候，url只写路径例如 “/login”。
 * --web-host：locust服务的web界面，用于配置 并发量 与 启动量。在web界面可以实时查看压测结果。
   + (如果是分布式，用于master，不用于slave)(理解的可能不对)
              
 * --slave：做分布式压测时，标记哪些用做分机。分机的主要任务是进行施压。
 
 * -f：脚本路径。可以写相对路径或是绝对路径。如果是脚本当前目录下，就写相对路径。如果不是，就写绝地路径。
 * --master-host： 做分布式压测时，指定主机的IP。只用于slave。如果没有指定，默认是本机“127.0.0.1”。
 * --master-port： 做分布式压测时，指定主机的port。只用于slave。如果没有指定且主机没有修改的话，默认是5557。
 * --master-bind-host： 做分布式压测时，指定分机IP。只用于master。如果没有指定，默认是所有可用的IP(即所有标记主机IP的slave)
 * --master-bind-port：做分布式压测时，指定分机port。默认是5557与5558。
 * -c： 用户数。
 * -r： 每秒启动用户数。比如想测试1000个虚拟用户对系统的压测，刚开始的时候是以10人/秒的速度开始递增到1000人。
 * -t： 运行时长。在t秒后停止。
 * -L：打印的日志级别，默认INFO。
 * --logfile：同-f
 * -V：查看Locust版本。
 * --host：同-H
 * PS: 如果参数是以“--”开头，则以=连接实参。例如“--host=http://sample”。
 如果不是，则以空格连接实参。例如“-H http://sample”
 * --show-task-radio ： 显示任务执行比例
   + 会计算两类比较，一种是单个Locust对象中task的权重比，一种是Locust对象权重比下task权重比
   + 先计算每个Locust对象，所占用的权重比：每个Locust对象中也有一个weight属性，默认是10
     >> 如：有两个Locust对象， WebUser1(weight=10), WebUser2(weight=10)
     >> 权重为WebUser1(weight1/weight1+weight2 = 0.5), WebUser2(weight2/weight1+weight2=0.5)
     
   + 计算单个Locust对象中，task权重比: weight=30其实是在tasks属性中添加了30个task方法
     >> 如：task1(weight=1), task2(weight=30)
     >> 权重比：task1(weight1/weight1+weight2=0.03), task2(weight2/weight1+wegith2=0.9)
   + 计算Locust对象权重比下的task权重比
     >> task1(weight1/((weight1+weight2)/0.5)=1.6), task2(weigth2/((weight1+weight2)/0.5)=48.4)
     >> task1的0.5是WebUser1的权重， task2的0.5是WebUser2的权重 
     >> 可以看出每个task在总的权重比
 * -t 或--run-time
   + 表示执行时间
   + 必须与--no-web参数使用
 * --no-web: 表示使用命令行模式。
   + 不带web界面。使用这个参数时，必须指定 -c、-r。
   
 * web模式
   + 如果没有配置--no-web参数且不是slave，则会启动web  
 
 
### 性能统计
 * 1.Type：请求类型；
 * 2.Name：请求路径；
 * 3.requests：当前请求的数量；
 * 4.fails：当前请求失败的数量；
 * 5.Median：中间值，单位毫秒，一半服务器响应时间低于该值，而另一半高于该值；
 * 6.Average：所有请求的平均响应时间，毫秒；
 * 7.Min：请求的最小的服务器响应时间，毫秒；
 * 8.Max：请求的最大服务器响应时间，毫秒；
 * 9.Content Size：单个请求响应的大小，单位字节；
 * 10.reqs/sec：每秒钟请求的个数。 
 
### 脚本增强
 * 关联
   + 从response中获取返回的数据，关联到下次请求中
 * 参数化
   + 循环取数据：模拟3用户并发请求网页，总共有100个URL地址，每个虚拟用户都会依次循环加载这100个URL地址
   + 保证并发测试数据唯一性，不循环取数据：模拟3用户并发注册账号，总共有90个账号，要求注册账号不重复，注册完毕后结束测试
   + 保证并发测试数据唯一性，循环取数据：模拟3用户并发登录账号，总共有90个账号，要求并发登录账号不相同，但数据可循环使用。
 * 检查点
 * 集合点
 
### statistics
 * 目前只有每秒请求数，平均响应时间，用户的增长曲线 三个图可看。
 
 
## idea中部署debug
 * SDK需要使用python2.7
   + 如果使用3.7导致monkey.patch_all()中thread模块失败
   
 * 引入模块
   + 目录/e/project/github/locust/locust
   + 必需是第2个locust
   
 * 写一个测试入口py
   + 在测试入口中引入locust.main
     > 注意不能直接在main中启动调试。因为main有相对路径引入的模块
   + test_local.py
       ```
        from locust.main import main
        
        
        if __name__ == '__main__':
            main()
        
       ```
   
 * 配置参数
 
 
 * 开始设置
 
 
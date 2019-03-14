## 搜索关键字
 * reactor例子
 * https://www.dnocm.com/ （微服务）
 * https://juejin.im/post/580103f20e3dd90057fc3e6d
   + 首看文章
   + 理解RxJava
   + 理解Observable(被观察者)/Observer（观察者）
   
 * https://juejin.im/post/582d413c8ac24700619cceed
   + 理解背压（Backpressure）
   + 背压是指在异步场景中，被观察者发送事件速度远快于观察者的处理速度的情况下，一种告诉上游的被观察者降低发送速度的策略
   + 背压是流速控制的一种策略
   + 背压策略的一个前提是异步环境，也就是说，被观察者和观察者处在不同的线程环境中
 * https://juejin.im/post/582b2c818ac24700618ff8f5
   + 理解RxJava 2.0
   + 新增Flowable(被观察者)/Subscriber(观察者)，支持背压
   + 其中Observable(被观察者)/Observer（观察者），不支持背压。(官方给出以1000个事件为分界线，仅供各位参考)
   
   
 * http://projectreactor.mydoc.io/?t=44479
 * https://blog.51cto.com/liukang/2094073
   + 接口API
 * http://wiki.jikexueyuan.com/project/reactor-2.0/25.html
 * https://my.oschina.net/yimingblog/blog/1789903
 * https://htmlpreview.github.io/?https://github.com/bumen/reactor-core/blob/master-zh/src/docs/index.html
 
### Hot 与 Cold Observable 
 * Cold Observable: 指的是那些在订阅之后才开始发送事件的Observable（每个Subscriber都能接收到完整的事件）。
   + 我们一般使用的都是Cold Observable
 * 2.0中Flowable(被观察者)/Subscriber(观察者)
   + 支持背压
 * Hot Observable: 指的是那些在创建了Observable之后，（不管是否订阅）就开始发送事件的Observable
   + 不支持背压 
   
### 响应式拉取
 * 在支持背压的被观察者与观察者中使用
 * 由观察者主动去被观察者那拉取数据，而不是由被观察者推送了。
   
### 其他观察者模式
 * Single/SingleObserver
 * Completable/CompletableObserver
 * Maybe/MaybeObserver
 
### 线程池
 * Schedulers.single
   + 单线程
 * Schedulers.elastic
   + 缓存池
 * Schedulers.parallel
   + 固定大小
 * 都返回一个Scheduler，（相当于Executor）
   + 调度器W
   + 是一个Worker线程池子，Scheduler拥有的并不是线程池，而是一个自行维护的ScheduledExecutorService池
   + 接收task, 把task分给Worker，由orker去执行Task
   + 自行维护
 * scheduler自行维护
   + 可供调遣的Worker。比如Schedulers.newParallel()返回的ParallelScheduler，
   其内维护的是一个固定大小的ScheduledExecutorService[]数组；
   而ElasticScheduler由一个ExecutorService的Queue来维护。
   + 任务分派策略。ElasticScheduler和ParallelScheduler都有一个pick()方法，用来选出合适的Worker。
   + 对于要处理的任务，包装为Callable，从而可以异步地返回一个Future给调用者。
 * Worker
   + 个Worker代表调度器可调度的一个工作线程，在一个Worker内，遵循FIFO（先进先出）的任务执行策略
   + 每个Worker都是一个ScheduledExecutorService
   
 * 相关操作符：publishOn
   + publishOn方法能够将onNext、onError、onComplete调度到给定的Scheduler的Worker上执行
   + 是由上到下（由被观察者到观察者）的流动
 * 相关操作符：subscribeOn
   + subscribeOn方法能够将subscribe（会调用onSubscribe）、request调度到给定的Scheduler的Worker上执行。
   + 是由下到上（由观察者到被观察者）的流动
   + 所以在任何位置增加一个subscribeOn(Schedulers.elastic())的话，都会借助自下而上的订阅链，通过subscribe()方法，将线程执行环境传递到“源头”
   继而影响到其后的操作符，直至遇到publishOn改变了执行环境
 * 有些操作符本身会需要调度器来进行多线程的处理，当你不明确指定调度器的时候，那些操作符会自行使用内置的单例调度器来执行。
 
### Flux ff = Flux.just(data).map(mapper).filter(predicate).subscribe(). 执行过程
 * 组装期：
   + fa = new FlaxArray(data) --> fm = new FluxMap(fa, mapper) --> ff = new FluxFilter(fm, predicate)
   --> 订阅期
 * 订阅期：由后向前, 在subscribeOn线程中
   + 传递订阅事件 <-- sa = new ArraySubscription(data) <-- fa.subscribe(sm) <-- sm = new MapSubscriber(sf, mapper) <-- fm.subscribe(sf) <-- sf = new FilterSubscriber(subscriber, predicate) <-- ff.subscribe(subscriber)
   
 * 传递订阅事件：, 在subscribeOn线程中
   + sm.onSubscribe(sa) --> sf.onSubscribe(sm) --> subscriber.onSubscribe(sf) -->传递请求
 * 传递请求：由后向前, 在subscribeOn线程中
   + 传递数据 <-- sa.request(n) <-- sm.request(n) <-- sf.request(n) 
 * 传递数据：在publishOn线程中
   + sm.onNext(v) --> sf.onNext(mv) --> subscriber.onNext(mv)
###
 * flatMap
   + 将每个Observable产生的事件里的信息再包装成新的Observable传递出来
 * map 
   + 输入的是原类型，返回转换后的类型
 * subscribeOn
   + 它指示Observable在一个指定的调度器上创建（只作用于被观察者创建阶段）。只能指定一次，如果指定多次则以第一次为准
   
 * observeOn
   + 指定在事件传递（加工变换）和最终被处理（观察者）的发生在哪一个调度器。可指定多次，每次指定完都在下一步生效。
   
### Spring WebClient
 * active connections: 表示连接池中现在有效的连接
 * inactive connections： 表示连接池中连接还未真正有效
 * 首先创建连接：
   + Created new pooled channel
   + 并放到连接池，此也连接还没有创建，所以是：now 0 active connections and 1 inactive connections
   + 如果连接池中有inactive连接，则不用创建直接使用空闲连接
 * 注册channel closeListener
   + 监听channel关闭后，将连接从连接池中释放
 * 连接成功：
   + Channel connected
   + now 1 active connections and 0 inactive connections。一个有效连接，0个无效连接
 * request_sent
   + 发送请求
 * response_received
   + 接收响应
 * disconnecting
   + channel执行close
 * Releasing
   + 连接池释放连接
 * Channel cleaned
   + 清理过程
   + now 0 active connections and 1 inactive connections 当前连接池状态
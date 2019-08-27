## sentinel 高可用

### sentinel概念
 * 由一个或多个sentinel实例组成的sentinel系统可以监视任意多个主服务器，以及这些主服务器属下的所有从服务器，
 并在被监视的主服务器进入下线状态时，自动将下线主服务器属下的某个从服务器升级为新的主服务器，然后由新的主服务器
 代替已下线的主服务器继续处理命令请求。
 
### 场景
 * server1: 为master服务器
 * server2: 为slave服务器
 * server3: 为slave服务器
 * server4: 为slave服务器
 
### 故障转移操作
 * 当server1下线时长超过了用户设置的下线时长上限时，sentinel系统就会执行故障转移
 * 过程
   + sentinel系统挑选server1下属的其中一个从服务器，将被选中的从服务器升级为主服务器
   + sentinel系统向server1属下的所有从服务器发送新的复制指令，让它们成为新的主服务器的从服务器，
   当所有从服务器都开始复制新的主服务器时，故障转移完成
   + sentinel系统还会监视已下线的server1，并在它重新上线时，将它设置为新的主服务器的从服务器
   
### 启动，初始化sentinel
 * 启动命令
   + redis-sentinel /path/to/your/sentinel.conf
   + redis-server /path/to/your/sentinel.conf --sentinel
 * 启动过程
   + 初始化服务器
   + 将普通redis服务器使用的代码替换为sentinel专用代码
   + 初始化sentinel状态
   + 根据给定的配置文件，初始化sentinel的监视主服务器列表
   + 创建连向主服务器的网络连接
   
   
 * 初始化sentinel状态
   ``` 
    struct sentinelState {
       // 当前纪元，用于实现故障转移
       uint64_t current_epoth;
       // 保存所有被这个sentinel监视的主服器
       // key: 主服务器名字
       // value: 指向sentinelRedisInstance结构的指针
       dict *masters
        
    }
   ```
 * 初始sentinel状态的masters属性
   + sentinelRedisInstance每个结构代表一个被sentinel监视的实例
      - 主服务器
      - 从服务器
      - 另外一个sentinel
   ``` 
    struct sentinelRedisInstance{
        // 标识值，记录实例的类型以及实例的当前状态
        int flags;
        // 实例名称
        // 主服务器名称由用户在配置文件中设置
        // 从服务器以及sentinel的名字由sentinel自动设置
        // 格式：ip:port
        char *name;
        // 实例运行id
        char *runid;
        // 配置纪元，用于实现故障转移
        uint64_t config_epoth;
        // 实例地址
        sentinelAddr *addr;
        // 实例无响应多少毫秒之后才会被判断为主现下线
        // sentinel down-after-milliseconds 设置
        mstime_t down_after_period;
        // 判断这个实例为客观下线所需要的支持投票数
        // sentinel monitor <master-name> <ip> <port> <quorum> 
        int quorum;
        // 在执行故障转移时，可以同时对新的主服务器进行同步的从服务器数量
        // sentinel parallel-syncs <master-name> <number>
        int parallel_syncs;
        // 刷新故障转移状态的最大时限
        // sentinel failover-timeout <master-name> <ms>
        mstime_t failover_timeout;
        
        // 主服务器属下的所有从服务器信息
        dict *slaves;
        // 其它同样监视这个主服务器的其它sentinel信息
        dict *sentinels;
    }
   ```
   + 配置
   ``` 
    sentinel monitor master1 127.0.0.1 6379 2
    sentinel down-after-milliseconds master1 30000
    sentinel parallel-syncs master1 1
    sentinel failover-timeout master1 900000
    
    sentinel monitor master2 127.0.0.1 6379 5
    sentinel down-after-milliseconds master2 50000
    sentinel parallel-syncs master2 5
    sentinel failover-timeout master2 4500000
   ```
   
 * 创建连接
   + sentinel将成为主服务器的客户端，可以向主服务器发送命令
   + sentinel会创建两个连向主服务器的异步网络连接
      - 一个命令连接：专门向主服务器发送命令
      - 一个订阅连接：专门用于订阅主服务器的__sentinel__:hello频道
      
 
### 获取主服务器信息
 * sentinel默认10秒一次频率，通过命令连接向主服务器发送info命令
 * 获取info信息
   + 获取主服务器信息，runid主服务器运行id, role主服务器角色
   + 获取主服务器属下的所有从服务器信息，用户无须提供从服务器信息，就可以自动发现从服务器
   + 创建或更新从服务器结构对象，并保存到主服务器结构的slaves属性中
   
### 获取从服务器信息
 * 当sentinel发现主服务器有新的从服务器时，除了创建从服务器相应的实例结构，还会创建到从服务器的命令连接和订阅连接。
 * 创建连接后，sentinel默认10秒一次频率通过命令连接向从服务器发送info命令
 * 获取info信息
   + runid: 从服务器运行id
   + role: 从服务器角色 
   + master_host: 连接主服务器的地址
   + master_port: 连接主服务器的端口
   + master_link_status: 主从连接状态
   + slave_priority: 从服务器优先级，用于选主
   + slave_repl_offset: 从服务器的复制偏移量，用于选主
   
### 向主服务器和从服务器发送信息
 * sentinel默认2秒一次频率，通过命令连接向所有被监视的主从服务器发送命令:
   + PUBLISH __sentinel__:hello "<s_ip>, <s_port>, <s_runid>, <s_epoch>, <m_name>, <m_ip>, <m_port>, <m_epoch>"
   + s开头是sentinel信息
   + m开头是sentinel监视的主服务器信息
   
### 接收来自主服务器和从服务器的频道信息
 * 当sentinel与一个主服务器或者从服务器建立起订阅连接之后，sentinel就会通过订阅连接，向服务器发送命令：
   + subscribe __sentinel__:hello
 * 对于每个与sentinel连接的服务器，sentinel即通过命令连接向服务器的__sentinel__:hello频道发送信息，
 又通过订阅连接从服务器的__sentinel__:hello频道接收信息
 * 对于监视同一个服务器的多个sentinel来说，一个sentinel发送的信息会被其它sentinel接收到
   + 用于sentinel之间相互认知
   + 用于更新其它sentinel对被监视服务器的认知
   
 * sentinel接收到消息处理
   + 分析s_runid，如果与自己的相同，说明这条信息是自己发送的，直接丢弃这条信息
   + 如果是其它sentinel，将更新对应信息
 
 * 更新sentinels字典
   + 是主服务器实例结构中的属性
   + 保存除sentinel自身之外，所有同样监视这个主服务器的基它sentinel的信息
   + 接收此消息的sentinel解析消息内容
      - s_ip, s_port, s_runid, s_epoch
      - m_name, m_ip, m_port, m_epoch
      - 先拿m_name去sentinelStats结构的masters属性中查找对应的主服务器信息
      - 再主服务器结构的sentinels属性中找s_ip:s_port对应的sentinel信息，更新或创建
   + 用户在使用sentinel时并不需要提供各个sentinel的地址信息，监视同一个主服务器的多个sentinel可以自动发现对方
   
 * 创建连身其它sentinel的命令连接
   + 当sentinel通过频道信息发现一个新的sentinel时，不仅为新的sentinel在sentinels字典中创建相应实例结构，还会
   创建一个连向新sentinel的命令连接，而新sentinel也同样会创建连向这个sentinel的命令连接
   
### 检测主观下线状态
 * sentinel默认每秒一次频率向所有与它创建了命令连接的实例，发送ping命令，通过回复来判断实例是否在线
   + 主服务器
   + 从服务器
   + 其它sentinel
   
 * 有效回复
   + +PONG
   + -LOADING
   + -MASTERDOWN
 * 无效回复
   + 除了有效回复，其它都是无效回复 
   
 * 如果一个实例在down-after-milliseconds毫秒内，连接向sentinel返回无效回复，那么sentinel会修改这个实例所对应
 的实例结构，修改flags属性中打开SRI_S_DOWN标识，标识此实例进入主观下线状态
   + 主观下线包括
     - 主服务器
     - 从服务器
     - 其它sentinel
     
 * 多个sentinel可能配置的主观下线时间不同，那么各自已各自的为准
 
### 检查客观下线状态
 * 当sentinel判断一个主服务器主观下线后为了确认这个主服务器是否真的下线了，它会向同样监视这一主服务器的其它sentinel
 进行询问，看它们是否也认为主服务器已经进入下线状态。
   + 当sentinel从其它sentinel那里接收到足够数量的已下线判断，sentinel就会将服务器判定为客观下线，并对主服务器执行
   故障转移
 * 发送sentinel is-master-down-by-addr命令
   + 格式：sentinel is-master-down-by-addr <ip> <port> <current_epoch> <runid>
      - ip, port: 主观下线的主服务器地址
      - current_epoch: sentinel当前配置纪元，用于选举头sentinel
      - runid: *或sentinel id。*表示：此命令用于检测主服务器客观下线状态。id表示：用于选举头sentinel
   + 询问其它sentinel是否同意主服务器已经下线
 * 接收sentinel is-master-down-by-addr命令
   + 接收sentinel检查master是否下线并回复
      - <down_state>: 1表示已下线，0表示未下线
      - <leader_runid>：*或id。*表示：此命令用于主服务器下线状态检测。id：用于选举
      - <leader_epoch>
   
 * 接收sentinel is-master-down-by-addr回复
   + 统计其它sentinel同意主服务器已经下线数量，当达到客观下线所需要数量时，sentinel修改主服务器实例结构
   flags属性打开SRI_O_DOWN，表示主服务器已经进入客观下线状态
   + 不同sentinel可能配置的quorum不同，则判断客观下线也不同，各自判断各自的
   
### 选举领头sentinel
 * 当一个主服务器被判断为客观下线时，监视这个下线主服务器的各个sentinel会进行协商，选举出一个领头sentinel，
 由领头sentinel对下线主服务器执行故障转移
 * 所有在线sentinel 都有被选为领头sentinel的资格，即监视同一个主服务器的多个在线sentinel中的任意一个都有可能
 成为领头sentinel
 * 每次进行领头sentinel选举之后，不论选举是否成功，所有sentinel的配置纪元(epoch)的值都会自增一次。
 * 在一个配置纪元里面，所有sentinel都有一次将某个sentinel设置为局部领头sentinel的机会，并且局部领头一旦设置，在
 这个配置纪元里面就不能再更改
 * 每个发现主服务器客观下线的sentinel都会要求其它sentinel将自己设置为局部领头
 * 当一个sentinel向另一个sentinel发送sentinel is-master-down-by-addr命令，并且命令 的runid参数不是*符号而是源
 sentinel运行id，这表示源sentinel要求目标sentinel将前者设置为后者的局部领头
 * sentinel设置局部领头sentinel的规则是先到先得：最先向目标sentinel发送设置要求的源sentinel将成为目标sentinel
 的局部领头sentinel，而之后接收到的所有设置要求都会被目标sentinel拒绝
 * 目标sentinel在接收到sentinel is-master-down-by-addr命令后，将向源sentinel回复的leader_runid和leader_epoch参数
 分别记录了目标sentinel的局部领头sentinel
 * 源sentinel在接收到目标sentinel返回的回复后，检查leader_epoch参数值和自己的配置纪元是否相同，相同话，那源sentinel
 继续取出leader_runid参数，如果leader_runid与源sentinel运行id一致，表示目标sentinel将源sentinel设置为了局部领头
 * 如果某个sentinel被半数已经上的sentinel设置成了局部领头sentinel,那么这个sentinel成为领头sentinel。如：在有10个sentinel
 组成的sentinel系统中，只有大于等于10/2+1=6个sentinel将某个sentinel设置为局部领头sentinel,就行
 * 因为领头sentinel的产生需要半数以上sentinel支持，并且每个sentinel在每个配置纪元里面只能设置一次局部领头sentinel,
 所以在一个配置纪元里面，只会出现一个领头sentinel.
 * 如果在给定时间，没有一个sentinel被选举为领头sentinel，那么各个sentinel将在一段时间之后再次进行选举，直到选出领头为止。
 
### 故障转移
 1. 在已下线主服务器属下的所有从服务器里面，挑选出一个从服务器并将其转换为主服务器
 2. 让已下线的主服务属下的所有从服务器改为复制新的主服务器
 3. 将已下线主服务器设置为新的主服务器的从服务器，当这个旧的主服务器重新上线时，它就会成为新的主服务器的从服务器
 
 * 选主
   + 挑选出一个状态好的，数据完整的从服务器，然后向这个服务器发送SLAVEOF no one命令，将这个从服务器转为主服务器
   + 查找规则
     - 先将所有从服务器保存到一个列表里
     - 删除所有处于下线或断线状态，保证列表中剩余的从服务器都是正常在线的
     - 删除列表中所有最近5秒没有回复过领头sentinel的info命令的从服务器，这可以保证列表中剩余的从服务器都最近
     成功进行过通信的
     - 删除所有与已下线主服务器连接断开超过down-after-milliseconds * 10 毫秒的从服务器：down-after-milliseconds
     选项指定了判断主服务器下线所需要时间，而删除断开时长越过down-after-milliseconds*10毫秒的从服务器，则可以
     保证列表中剩余的从服务器都没有过早地与主服务器开连接，即列表中剩余的从服务器保存的数据都是比较新的
     - 根据从服务器的优先级，对列表剩余的从服务器进行排序，并选出其中优先级最高的从服务器
     - 如果有多个具有相同最高优先级的从服务器，那么领头sentinel将按照从服务器的复制偏移量，选出其中偏移量最大的
     的从服务器
     - 最后如果有多个优先级最高、复制偏移最大的从服务器，那么按照运行id对这些从服务器排序，选出运行id最小的从服务器
   + 在发发slaveof no one命令之后，领头sentinel会以每秒一次的频率（平时是每十秒一次）向被升级的从服务器发送info命令
   并观察回复中的角色信息。当role从原来slave变成master时，sentinel知道已经升级这主服务器了
   
 * 修改从服务器的复制目标
   + 让已下线主服务器的属下所有从服务器复制新的主服务器。通过向从服务器发送slaveof命令实现
   `slaveof new_master_ip new_master_port`
   
 * 将旧的主服务器变为从服务器
   + 当旧的主服务器重新上线时，sentinel就会向它发送slaveof命令，让它成为从服务器
   
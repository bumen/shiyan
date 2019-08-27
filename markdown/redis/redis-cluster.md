## redis集群
 * 集群通过分片来进行数据共享，并提供复制和故障转移功能
 * 概念
   + 节点
   + 槽指派
   + 命令执行
   + 重新分片
   + 转向
   + 故障转移
   + 消息

### 节点
 * 一个集群通常由多个节点组成。开始时每个节点都相互独立。我们必须将节点连接起来构成一个集群
 * 节点连接
   + cluster meet <ip> <port>
   + 向一个节点node发送cluster meet命令，可以让node节点与ip和port所指定进行握手，当握手成功时，node节点就会将
   ip和port所指定节点添加到node节点当前所在的集群中
   
 * 启动节点
   + 配置：cluster-enabled [yes|no] 
   + 服务器跟据选项是否yes，来决定是否开启服务器集群模式。否则为普通服务器
   
 * 集群数据结构
   + clusterNode结构保存了节点的当前状态，如：创建时间，节点名字，ip，port等
   + clusterState 代表集群的状态
   + 伪代码 
   ``` 
        struct clusterNode{
            # 创建时间
            mstime_t ctime;
            
            # 名字，40个十六进制字符组成
            char name[redis_cluster_namelen];
            
            # 节点标识，
            int flags;
            
            # 配置纪元，用于故障转移 
            uint64_t configEpoth;
            
            # ip
            char ip[redis_ip_str_len];
            # 端口
            int port;
            
            # 保存连接节点所需的有关信息
            clusterLink *link;
        }
        
        typedef struct clusterLink{
            # 创建时间
            mstime_t ctime;
            # 套接字描述符
            int fd;
            # 输出缓冲区
            sds sndbuf;
            
            # 输入缓冲区
            sds rcvbuf;
            # 与这个连接相关联的节点
            struct clusterNode *node;
        }
        
        typdef struct clusterState{
            # 指向当前节点指针
            clusterNode *myself;
            # 集群当前配置纪元
            uint64_t currentEpoth;
            # 集群当前状态，在线，下线
            int state;
            # 集群中至少处理着一个槽的节点数量
            int size;
            # 集群节点名单（包括myself）
            # 字典的键为节点的名字，字典的值为节点对应的clusterNode结构
            dict *nodes;
        }
   ```
   
 * cluster meet 命令实现
   + 步骤
      1. 节点A会为节点B创建一个clusterNode结构，并将该结构添加到自己的clusterState.nodes字典里面
      2. 之后，节点A将根据cluster meet 命令的ip, port, 向节点B发送一条meet消息
      3. 如果顺利，节点B将接收到节点A发送的Meet消息，节点B会为节点A创建一个clusterNode结构，并将该结构添加到自己的
      clusterState.nodes字典里
      4. 之后，节点B将向节点A返回一条PONG消息
      5. 如果一切顺利，节点A将接收到节点B返回，通过这条消息节点A知道节点B已经成功接收了自己发送的meet消息
      6. 之后，节点A将向节点B返回一条PING消息
      7. 如果一切顺利，节点B将接收到节点A返回的PING消息，通过这条PING消息节点B可以知道节点A已经成功地接收了自己返回
      PONG消息，握手完成。
      
 * cluster nodes
   + 查看集群节点信息
      
  
### 槽指派
 * redis集群通过分片方式来保存数据库中的键值对，集群的整个数据库被分为16384个槽，数据库中每个键都属于这16384个槽的其中一个
 * 当数据库中的16384个槽都节点在处理时，集群处于在线状态，相反地，如果数据库中有任何一个槽没有得到处理，那么集群处于下线状态
 * cluster info
   + 查看集群在线状态
 * cluster addslots <slot> [slot ...]
   + 可以将一个或多个槽指派给节点负责
   + 如：在某个节点上执行：cluster addslots 0 1 2 3 4 5 ... 5000 表示将0-5000的槽指派给这个节点
   
 * 记录节点的槽指派信息
   + clusterNode结构的slots属性和numslot属性记录节点负责处理哪些槽
   + 伪代码
   ``` 
        struct clusterNode{
            # 数组长度16384/8 = 2048个字节，共16384个二进近位，0-16383
            # 如果slots[i] = 1, 表示节点负责这个槽位，否则不负责
            unsigned char slots[16384/8];
            # 负责槽位数量
            int numslots;
        }
   ```
   
 * 传播节点的槽指派信息
   + 一个节点除了将自己负责处理的槽记录在clusterNode结构中，它还会将自己的slots数组通过消息发送给集群中其他节点
   + 如：节点A通过消息从节点B接收到节点B的slots数组时，节点A会在自己的clusterState.nodes字典中查找节点B对应的
   clusterNode结构，并对结构中slots数组进行保存或更新
   + 因此集群中每个节点都会知道数据库中的16384个槽分别被指派给了集群中的哪些节点
   
 * 记录集群所有槽的指派信息
   + clusterState结构中的slots数组记录了集群中所有16384个槽的指派信息
   + 伪代码
   ``` 
        clusterNode *slots[16384];
   ```
   + 如果slots[i] = null 那么槽i未指派给任何节点
   + 如果slots[i] = clusterNode结构，那么槽i指派给了clusterNode所代表的节点
   
 * cluster addslots命令实现
   + 伪代码
   ```
        def cluster_addslots(*all_input_slots):
            # 遍历所有输入槽，检查它们是否都是未指派槽
            for i in all_input_slots:
                # 如果有哪怕一个槽已经被指派给了某个节点
                # 那么向客户端返回错误，并终止命令执行
                if clusterState.slots[i] != null:
                    reply_error();
                    return;
                    
            # 如果所有输入槽都是未指派
            # 那么再次遍历所有输入槽，将这些槽指派给当前节点
            for i in all_input_slots:
                # 设置clusterState结构的slots
                clusterState.slots[i] = clusterState.myself
                # 设置clusterNode结构的slots
                setSlotBit(clusterState.myself.slots, i)
   ```
   
### 在集群中执行命令
 * 在对数据库中的16384个槽都进行了指派之后，集群就会进入上线状态，这时客户端就可以向集群中的节点发送数据命令了
 * 当客户端向节点发送数据库键有关的命令时，接收命令的节点会计算出命令要处理的数据库键属于哪个槽，并检查这个槽是否
 指派给了自己
   + 如果键所在的槽正好就是指派给了当前节点，那么节点直接执行这个命令
   + 如果键所在的槽并没有指派当前节点，那么节点会向客户端返回一个MOVED错误，指引客户端转向至正确的节点，并再次发送
   之前想要执行的命令
   
 * 计算键属于哪个槽
   + 伪代码
   ``` 
        def slot_number(key):
            return CRC16(key) & 16384
   ```
   + 命令查看
      - cluster keyslot <key>
      
 * 判断槽是否由当前节点负责处理
   + 当节点计算出键所属槽i之后，节点就会检查自己在clusterState.slots数组中的项i, 判断键所在的槽是否由自己负责
      - 如果clusterState.slots[i] == clusterState.myself, 那么说明槽i由当前节点负责，节点可以执行客户端命令
      - 如果不等于，说明槽i并非由当前节点负责，节点会根据clusterState[i]指向的clusterNode所记录的ip, port
      向客户端返回MOVED错误，指引客户端转向至正在处理槽i的节点上
      
 * MOVED错误
   + 当节点发现键所在槽并非由自己负责处理的时候，节点就会向客户端返回一个MOVED错误
   + 格式  
   `MOVED <slot> <ip>:<port>`
      - slot为键所在槽
      - ip为槽所在的节点
      
   + 一个集群客户端通常会与集群中的多个节点创建套接字连接，而所谓的节点转向实际上就是换一个套接了来发送命令
   + 如果还没有与MOVED指定的ip,port创建连接，则先连接再跳转
   
 * 节点数据库实现
   + 节点和单机服务器在数据库方面的一个区别是，节点只能使用0号数据库，而单机redis服务器则没有这一限制
   + 节点将健值对保存在数据库里面之外，节点还会用clusterState结构中的slots_to_keys跳跃表来保存槽和键之间的关系
   ``` 
        typedef struct clusterState{
            zskiplist *slots_to_keys;
        }
   ```
   + slots_to_keys跳跃表每个节点的分值都是一个槽号，而每个节点的成员都是一个数据库键
      - 每当节点往数据库添加一个新的键值对时，节点就会将这个键以及键的槽号关联至这个属性跳跃表
      - 当节点删除数据库中的某个键时，节点就会在这个属性跳跃表解除关联
   + 通过slots_to_keys跳跃表记录各个数据库所属的槽，节点可以很方便地对属于某个或某些槽的所有数据库键进行批量操作
   + cluster getkeysinslot <slot> <count>命令可以返回最多count个属于槽slot的数据库键
   
 * 重新分片 
   + 重新分片操作可以将任意数量已经指派给某个节点的槽改为指派给另一个节点，并且相关槽所属的键值对也会从源节点被移动
   到目标节点
   + 重新分片可以在线进行，重新分片过程中，集群不需要下线，并且源节点和目标节点都可以继续处理命令请求
   + 实现原理
      - 重新分片操作是由redis的集群管理软件redis-trib负责执行，redis提供了重新分片所需的命令，而redis-trib则通过
      向源节点和目标节点发送命令进行重新分片操作
      1）redis-trib对目标节点发送cluster setslot <slot> importing <source_id>命令，让目标节点准备好从源节点
      导入属于槽slot 的键值对
      2）redis-trib对源节点发送cluster setslot <slot> migrating <target_id>命令，上源节点准备好将属于槽slot
      的键值迁移至目标节点
      3）redis-trib向源节点发送cluster getkeysinslot <slot> <count>命令，获得最多count个键
      4）对于步骤3获得的每个键名，redis-trib都向源节点发送一个migrate <target_ip> <target_port> <key_name> 0 <timeout>
      命令，将被选中的键原子地从源节点迁移至目标节点
      5）重复执行步骤3，4，直到源节点保存的所有属于槽slot的键值对都被迁移至目标节点为止。
      6）redis-trib向集群中的任意一个节点发送cluster setslot <slot> node <target_id>命令，将槽slot指派给目标节点
      这一指派信息会通过消息发送至整个集群，最终集群中所有节点都会知道槽slot已经指派给了目标节点
      
      - 如果多个槽重新分片，则每个槽都按上面步骤分别执行
      
 * ASK错误
   + 重新分片期间，可能出现一部分键值对保存在源节点里面，而一部分键值对则保存在目标节点
   + 当客户端查询的命令正好是正在迁移的槽时
      - 源节点会先在自己的数据库中查找指定的键，如果找到，就直接返回
      - 相反，如果源节点没有，那么键可能被迁移到目标节点，则源节点向客户端返回一个ASK错误，指引客户端转向正在导入槽
      的目标节点，并再次发送之前的命令
      
      
   + cluster setslot <slot> importing 实现
      - clusterState结构的importing_slots_from数组记录了当前节点正在从其它节点导入的槽
      - 伪代码
      ``` 
            typedef sturct clusterState{
                clusterNode *importing_slots_from[16384]
            }
      ```
      - 如果importing_slots_from[i]的值不为null,而是指向一个clusterNode结构，那么表示当前节点正在从这个节点导入槽i
      - 命令如：cluster setslot 100 importing 99adsfljasdfk2334234klasdf
      
   + cluster setslot <slot> migrating命令
      - clusterState结构的migrating_slots_to数组记录了当前节点正在迁移至其他节点的槽
      - 伪代码
      ``` 
            typedef struct clusterNode{
                clusterNode *migrating_slots_to{16384];
            }
      ```
      - 如果migrating_slots_to[i] 指向一个clusterNode结构，那么表示当前节点将槽i迁移至clusterNode所代表的节点
      
   + ASK错误
      - 返回命令：ask 16198[slot] ip:port
      - 客户端收到ask后转向至正在导入槽的目标节点，然后首先向目标节点发送一个asking命令，之后再重新发送原本想要执行的命令
      
   + ASKING命令
      - asking命令唯一要做的就是打开发送该命令的客户端redis_asking标识，
      - 伪代码
      ``` 
            def asking():
                client.flags |= redis_asking
                relpy("ok")
      ```
      - 正常情况下如果客户端向节点发送一个关于槽i命令，而槽i又没有指派给这个节点话，那么节点返回客户端MOVED错误
      但如果节点clusterState.imporing_slots_from[i] 显示节点正在导入槽i，并且发送命令的客户端带有redis_asking
      标识，那么节点将破例执行这个命令一次。之后执行完命令客户端redis_asking标识被清除
      
### 复制与故障转移 
 * redis集群中的节点分为主节点master和从节点slave，其中主节点用于处理槽，而从节点用于复制某主节点，并在被复制的主节点 
 下线时，代替下线主节点继续处理命令请求
 
 * 设置从节点
   + 命令 
   `cluster replicate <node_id>`
   + 可以让接收此命令的节点成为 node_id所指定节点的从节点，并开始对主节点进行复制
   + 接收到此命令的节点首先会在自己的clusterState.nodes字典中找node_id 所对应节点的clusterNode结构，并将自己
   clusterState.myself.slaveof指针指向这个结构，以此来记录这个节点正在复制的主节点
   + 伪代码
   ``` 
        struct clusterNode {
            struct clusterNode *slaveof;
        }
   ```
   + 然后节点会修改自己在clusterState.myself.flags中的属性，关闭原本的redis_node_master标识，打开redis_node_slave标识
   + 最后节点会调用复制代码，并根据clusterState.myself.slaveof指向的clusterNode结构所保存的ip和port，对主节点
   进行复制，因为节点复制与单机复制功能使用了相同的代码，所在让节点复制主节点相当于向从节点发送命令
   slaveof <master_ip> <master_port>
   + 一个节点成为从节点，并开始复制某个节点这一信息会通过消息发送给集群中的其他节点，最终集群中的所有节点都会知道某个
   节点正在复制某个节点
   + 集群中所有节点都会在代表主节点的clusterNode结构的slaves属性和numslaves属性中记录正在复制这个主节点的从节点
   + 伪代码
   ``` 
        struct clusterNode{
            int numslaves;
            // 每个数组项指向一个正在复制这个节点的从节点的clusterNode结构
            struct clusterNode **slaves;
        }
   ```
   
 * 故障检测
   + 集群中的每个节点都会定期向集群中的其它节点发送ping消息，以此来检测对方是否在线。如果在规定时间内没有收到节点返回的
   PONG消息，那么发送ping消息节点就会将对方标记为疑似下线
   + 集群中各个节点会通过互相发送消息的方式交换集群中各个节点的状态信息
     - 在线状态
     - 疑似下线状态（pfail）
     - 已下线状态（fail）
   + 当一个主节点A通过消息得知主节点B认为主节点C进入了疑似下线状态时，主节点A会在自己的clusterState.nodes字典中找到
   主节点C所对应的clusterNode结构，并将主节点B的下线报告添加到clusterNode结构的fail_reports链表中
   + 伪代码
   ``` 
        struct clusterNode{
            # 一个链表，记录了所有其他节点对节点的下线报告
            list *fail_reports;
        }
        
        struct clusterNodeFailReport{
            # 报告目标节点已经下线的节点
            struct clusterNode *node;
            # 最后一次从node收到下线报告的时间
            # 程序使用这个时间检测下线报告是否过期 
            mstime_t time;
        }
   ```
   + 如果在一个集群中，半数以上负责处理槽的主节点都将某个主节点x报告为疑似下线，那么这个主节点x将被标记为已下线，
   将主节点x标记为已下线的节点会向集群广播一条关于主节点x的fail消息，所有收到这条fail消息的节点都会立即将主节点x标记
   为已下线
   
 * 故障转移
   + 当一个从节点发现自己正在复制的主节点进入下线状态时，从节点将开始对下线主节点进行故障转移
     1）复制下线主节点的所有从节点里面，会有一个节点被选中
     2）被选中的从节点会执行slaveof no one命令，成为新的主节点
     3）新的主节点会撤销所有对已经下线主节点的槽指派，并将这些槽信息全部指派给自己
     4）新的主节点向集群广播一条PONG消息，这条PONG消息可以让集群中的其他节点立即知道这个节点已经由从节点变成了主节点
     并且这个主节点已经接管了原本已下线节点负责处理的槽
     5）新的主节点开始接收和自己负责处理的槽有关命令请求，故障转移完成 
     
 * 选举新的主节点
   1）集群的配置纪元是一个自增计数器，它的初始值为0
   2）当集群里的某个节点开始一次故障转移操作时，集群配置纪元的值会自增1
   3）对于每个配置纪元，集群里每个负责处理槽的主节点有一次投票的机会，而第一个向主节点要求投票的从节点将获得主节点的投票。
   4）当从节点发现自己正在复制的主节点进入已经下线状态时，从节点会向集群广播一条
   cluster_type_failover_auth_request消息，要求所有收到这条消息，并且具有投票权的主节点向这个从节点投票
   5）如果一个主节点具有投票权（它正负责处理槽），并具这个主节点尚未投票给其他从节点，那么主节点将向要求投票的从节点回
   一条cluster_type_failover_auth_ack消息，表示这个主节点支持从节点成为新的主节点
   6）每个参与选举的从节点都会收到cluster_type_failover_auth_ack消息，并根据自己收到了多少条这种消息来统计自己
   获得了多少主节点的支持
   7）如果集群里有N个具有投票权的主节点，那么当一个从节点收集到大于等于N/2+1张支持票时，这个从节点会当选为新的主节点
   8）因为在每一个配置纪元里面，每个具有投票权的主节点只能投一次票，所以如果有N个主节点进行投票，那么具有大于等于n/2+1
   张支持票的从节点只会有一个，这确保了新的主节点只会有一个。
   9）如果在一个配置纪元里面没有从节点收集到足够多的支持票，那么集群进入一个新的配置纪元，并再次进行选举，直到选举成功
   
 * 消息
   + 集群中各个节点通过发送和接收消息来进行通信。
   + 节点发送的消息主要有5种：
      - meet消息：当发送者接收到客户端发送的cluster meet命令时，发送者会向接收者发送meet消息，请求接收者加入到发送者
      当前所在的集群里
      
      - ping消息：集群里的每个节点默认每隔一秒就会从已知节点列表中随机选出5个节点，然后对这五个节点中最长时间没有发送过ping
      消息的节点发送ping消息，以此来检测被选中的节点是否在线。除此之外，如果节点A最后一次收到节点B发送的PONG消息的时间
      距离当前时间已经超过了节点A的cluster-node-timeout选项设置时长的一半，那么节点A也会向节点B发送ping消息，这可以
      防止节点A因为长时间没有随机选中节点B作为ping消息的发送对象而导致对节点B的信息更新滞后。
      
      - pong消息：当接收者收到发送者发来的meet消息或者ping消息时，为了向发送者确认这条meet消息或ping消息已经到达。
      接收者会向发送者返回一条pong消息。另外，一个节点也可以通过向集群广播自己的pong消息来让集群中的其他节点立即刷新
      关于这个节点的认识。如：当一次故障转移操作成功后，新的主节点会向集群广播一条pong消息，让集群中的其他节点立即
      知道这个节点已经变成了主节点
      - fail消息：当一个主节点A判断另一个主节点B已经进入fail状态时，节点A会向集群广播一条关于节点B的fail消息，所有
      收到这条消息的节点会立即将节点B标记为已下线
      
      - publish消息：当节点接收到一条publish消息时，节点会执行这个命令，并向集群广播一条publish消息，所有接收到这条
      publish消息的节点都会执行相同的Publish命令
      
   + 消息头
      - 伪代码
      ``` 
        typedef struct{
            # 消息长度（包括消息头与消息正文的长度）
            uint32_t totlen;
            # 消息类型
            uint16_t type;
            # 消息正文包括的节点信息数量
            # 只在发送meet, ping, pong这三种gossip消息时使用
            uint16_t count;
            # 发送者所处的配置纪元
            uint64_t currentEpoch;
            # 如果发送者是一个主节点，那么这里记录的是发送者的配置纪元
            # 如果发送者是一个从节点，那么这里记录的是发送者正复制的主节点配置纪元
            uint64_t configEpoch;
            # 发送者名字
            char sender[redis_cluster_namelen]; 
            # 发送者目前的槽指派信息
            unsigned char myslots[redis_cluster_slots/8];
            
            # 如果发送者是一个从节点，那么这里记录的是发送者正在复制的主节点名字
            # 如果发送者是一个主节点，那么这里记录的是redis_node_null_name
            char slaveof[redis_cluster_namelen];
            # 发送者端口号
            uint16_t port;
            # 发送者状态
            uint16_t flags;
            # 发送者所处的集群状态
            unsigned char state;
            # 消息正文
            union clusterMsgData data;
        }
        
        union clusterMsgData{
            # meet, ping, pong消息正文
            struct{
                # 每条meet, ping, pong 消息都包含两个clusterMsgDataGossip结构
                clusterMsgDataGossip gossip[l];
            }
            
            # fail消息正文
            struct{
                clusterMsgDataFail about;
            }
            
            # publish消息正文
            struct{
                clusterMsgDataPublish msg;
            }
            
            // 其它正文
        }
      ```
   + meet, ping, pong 消息实现
      - 每次发送meet, ping, pong 消息时，发送者都从自己的已知节点列表中随机选出两个节点（可以是主或从节点），并将
      这两个节点信息分别保存到两clusterMsgDataGossip结构里
      - clusterMsgDataGossip结构记录了选中节点的名字，发送者与被选中节点最后一次发送和接收到ping消息和pong消息的时间戳，
      被选中节点的Ip, port, flags
      - 伪代码
      ``` 
        typedef struct{
            char nodename[redis_cluster_namelen];
            # 最后一次向该节点发送ping消息的时间
            uint32_t ping_sent;
            # 最后一次从该节点接收到pong消息的时间
            uint32_t pong_received;
            
            char ip[16];
            
            uint16_t port;
            
            uint16_t flags;
        }
      ```
      - 接收者处理消息
      1) 如果被选中节点不存在接收者的已知节点列表，那么说明接收者是第一次接触到被选中节点。与被选中节点握手
      2）如果被选中节点已经存在，则会对被选中节点对应的clusterNode结构进行更新
      
   + fail消息
      - 当集群里的主节点A将主节点B标记不已下线时，主节点A将向集群广播一条关于主节点B的fail消息，所有接收到这条fail
      消息的节点都将主节点B标记为已下线
      - 伪代码
      ``` 
        # 只记录已下线节点的名字
        typedef struct{
            char nodename[redis_cluster_namelen];
        } clusterMsgDataFail
      ```
      
   + publish消息
      - 当客户端向集群中的某个节点发送命令：  
      `publish <channel> <message>`
      时，接收到publish命令的节点，不仅会向channel频道发送消息，还会向集群中广播一条Publish消息，所有接收到这条消息
      的节点都会向channel频道发送message消息
      - 伪代码
      ``` 
        typedef struct{
            # channel参数长度
            uint32_t channel_len;
            # message参数长度
            uint32_t message_len;
            # 定义8字节只是为了对齐其它消息结构，实际长度由保存内容决定 
            # 保存是channel参数和message参数
            unsigned char bulk_data[8];
        }
      ```
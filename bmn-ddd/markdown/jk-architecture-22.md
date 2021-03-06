### 第22章，CAP理论

 * 在一个分布式系统（指互相连接并共享数据的节点的集合）中，当涉及读写操作时，
 只能保证一致性（Consistence）、可用性（Availability）、分区容错性（Partition Tolerance）三者中的两个，
 另外一个必须被牺牲。
   + CAP 理论探讨的分布式系统：强调了两点::指互相连接并共享数据的节点的集合
      > 分布式系统并不一定会互联和共享数据。如Memcache
   + CAP 关注的是对数据的读写操作，而不是分布式系统的所有功能
   
 * 一致性（Consistency）
   + 对某个指定的客户端来说，读操作保证能够返回最新的写操作结果
   
 * 可用性（Availability）
   + 非故障的节点在合理的时间内返回合理的响应（不是错误和超时的响应）。
      - 明确了不能超时、不能出错，结果是合理的，注意没有说“正确”的结果。
      - 例如，应该返回 100 但实际上返回了 90，肯定是不正确的结果，但可以是一个合理的结果。也符合A
     
 * 分区容忍性（Partition Tolerance）
   + 当出现网络分区后，系统能够继续“履行职责”。
   + 分区就是集群中一部分节点和另外一部分无法通信
   
   
 * CAP 应用
   + 虽然 CAP 理论定义是三个要素中只能取两个，但放到分布式环境下来思考，
   我们会发现必须选择 P（分区容忍）要素，因为网络本身无法做到 100% 可靠，有可能出故障，所以分区是一个必然的现象。
   > 所以CAP关注的是分区时的可用性和一致性
   ``` 
        如果我们选择了 CA 而放弃了 P，那么当发生分区现象时，为了保证 C，系统需要禁止写入，当有写入请求时，
        系统返回 error（例如，当前系统不允许写入），这又和 A 冲突了，因为 A 要求返回 no error 和 no timeout。
        因此，分布式系统理论上不可能选择 CA 架构，只能选择 CP 或者 AP 架构。
   ```
   1. CP - Consistency/Partition Tolerance
     + 当发生分区时，node1与node2数据不一致，则不允许读，即读都返回错误则不满足可用性。
   2. AP - Availability/Partition Tolerance
     + 当发生分区时，node1与node2数据不一致，则允许读，允许读到不一致数据（虽然不正确但是合理的结果），此时不满足一致性。
     
     
### Paxos理解
 * paxos的核心思想是少数服从多数，
   + 在节点数为2n+1的集群中，只要n+1节点上完成了写入就能保证接下来的读操作能够读到最新的值。
   从这一点来说它满足C（对某个指定的客户端来说，读操作保证能够返回最新的写操作结果);
   + 一个实现了paxos协议的集群最多可以容忍n个节点故障（n个节点同时故障的概率是比较小的），
   非故障节点组成的集群仍能正常提供服务，从这个角度来讲它满足A(非故障的节点在合理的时间内返回合理的响应，不是错误和超时的响应)；
   + paxos集群发生分区肯能存在两种情况，第一种情况是发生分区后没有发生重新选举，这种情况下集群仍能正常工作，因此满足P(当出现网络分区后，系统能够继续“履行职责”)。
   另一种情况是发生分区后原来的集群达不到多数派，集群不在对外提供服务，因此不满足P，当发生这种情况的时候，一般会快速修复。
   总的来说在某种意义上来看paxos满足CAP。
   
``` 
   Paxos算法本身能提供的是，可靠的最终一致性保证。如有足够的隔离性措施，中间状态的无法被客户端读取，则可以达到强一致性，这种属于CP架构。其它情况，就是AP架构。
   CAP定理存在不少坑点，理解起来很是令人费解。
   1、适用场景。分布式系统有很多类型，有异构的，比如节点之间是上下游依赖的关系，有同构的，比如分区/分片型的、副本型的（主从、多主）。CAP定理的适用场景是副本型的这种。
   2、一致性的概念，从强到弱，线性一致性、顺序一致性、因果一致性、单调一致性、最终一致性，CAP中的一致性应该是指顺序一致性。
   3、CAP中的一致性，与ACID中的一致性的区别。事务中的一致性，是指满足完整性约束条件，CAP中的一致性，是指读写一致性。
   4、CAP中的可用性，与我们常说的高可用的区别。比如HBase、MongoDB属于CP架构，Cassandra、CounchDB属于AP系统，能说后者比前者更高可用么？
   应该不是。CAP中的可用性，是指在某一次读操作中，即便发现不一致，也要返回响应，即在合理时间内返回合理响应。
   我们常说的高可用，是指部分实例挂了，能自动摘除，并由其它实例继续提供服务，关键是冗余。
   5、哪些情况属于网络分区。网络故障造成的分区，属于。节点应用出现问题导致超时，属于。节点宕机或硬件故障，不属于。
   
   1、Paxos算法本身是满足线性一致性的。线性一致性，也是实际系统能够达到的最强一致性。
   2、Paxos及其各种变体，在实际工程领域的实现，大多是做了一定程度的取舍，并不完全是线性一致性的。
   3、比如，Zookeeper和Etcd，都是对于写操作（比如选举），满足线性一致性，对于读操作未必满足线性一致性。
   即可以选择线性一致性读取，也可以选择非线性一致性读取。这里的非线性一致性，就是顺序一致性。
   4、cap中的一致性，是指线性一致性，而不是顺序一致性。
```

### 第23章 CAP细节
 * 我们在进行架构设计时，整个系统要么选择 CP，要么选择 AP。
 但在实际设计过程中，每个系统不可能只处理一种数据，而是包含多种类型的数据，有的数据必须选择 CP，有的数据必须选择 AP。
 而如果我们做设计时，从整个系统的角度去选择 CP 还是 AP，就会发现顾此失彼，无论怎么做都是有问题的。
   + 所以在 CAP 理论落地实践时，我们需要将系统内的数据按照不同的应用场景和要求进行分类，
   每类数据选择不同的策略（CP 还是 AP），而不是直接限定整个系统所有数据都是同一策略。
   
 * CAP 是忽略网络延迟的。
   + 技术上是无法做到分布式场景下完美的一致性的。而业务上必须要求一致性，
   因此单个用户的余额、单个商品的库存，理论上要求选择 CP 而实际上 CP 都做不到，只能选择 CA。
   也就是说，只能单点写入，其他节点做备份，无法做到分布式情况下多点写入。
      - 不能用分布式就不要硬用。可以采用其它方式实现
      - 只是说“单个用户余额、单个商品库存”无法做分布式，但系统整体还是可以应用分布式架构的。
      ``` 
        id: 0-100在node1, 101-200在node2。但node2挂了不会影响node1
      ```
      
 * CAP 理论告诉我们分布式系统只能选择 CP 或者 AP
   + 但其实这里的前提是系统发生了“分区”现象。
   如果系统没有发生分区现象，也就是说 P 不存在的时候（节点间的网络连接一切正常），我们没有必要放弃 C 或者 A，应该 C 和 A 都可以保证，
   这就要求架构设计的时候既要考虑分区发生时选择 CP 还是 AP，也要考虑分区没有发生时如何保证 CA。
   
   + 放弃并不等于什么都不做，需要为分区恢复后做准备
     - CAP 理论的“牺牲”只是说在分区过程中我们无法保证 C 或者 A，但并不意味着什么都不做
     - 因为在系统整个运行周期中，大部分时间都是正常的，发生分区现象的时间并不长。
     - 分区期间放弃 C 或者 A，并不意味着永远放弃 C 和 A，我们可以在分区期间进行一些操作，从而让分区故障解决后，系统能够重新达到 CA 的状态。
     
 * CAP与ACID区别
   + ACID 是数据库管理系统为了保证事务的正确性而提出来的一个理论，ACID 包含四个约束
   + ACID 中的 C 是指数据库的数据完整性，而 CAP 中的 C 是指分布式节点中的数据一致性
   
 * CAP与BASE
   + BASE 是指基本可用（Basically Available）、软状态（ Soft State）、最终一致性（ Eventual Consistency）
   + 核心思想是即使无法做到强一致性（CAP 的一致性就是强一致性），但应用可以采用适合的方式达到最终一致性
   1. 基本可用（Basically Available）
     + 分布式系统在出现故障时，允许损失部分可用性，即保证核心可用。
   2. 软状态（Soft State）
     + 允许系统存在中间状态，而该中间状态不会影响系统整体可用性。这里的中间状态就是 CAP 理论中的数据不一致。
   3. 最终一致性（Eventual Consistency）
     + 系统中的所有数据副本经过一定时间后，最终能够达到一致的状态。
     
   + BASE 理论本质上是对 CAP 的延伸和补充，更具体地说，是对 CAP 中 AP 方案的一个补充
   
   + CAP 理论是忽略延时的，而实际应用中延时是无法避免的。
     - 因此 CAP 中的 CP 方案，实际上也是实现了最终一致性，只是“一定时间”是指几毫秒而已
   + AP 方案中牺牲一致性只是指分区期间，而不是永远放弃一致性。
     - 这一点其实就是 BASE 理论延伸的地方，分区期间牺牲一致性，但分区故障恢复后，系统应该达到最终一致性
     
 * 综合上面的分析
   + ACID 是数据库事务完整性的理论
   + CAP 是分布式系统设计理论
   + BASE 是 CAP 理论中 AP 方案的延伸。  
   
    
### 墨菲定律
 * 可能出错的事情最终都会出错
 
### 第24章 FMEA方法，排除架构可用性隐患的利器
 * FMEA 是高可用架构设计的一个非常有用的方法，能够发现架构中隐藏的高可用问题
 * （Failure mode and effects analysis，故障模式与影响分析）
 * FMEA分析方法
   + 给出初始的架构设计图
   + 假设架构中某个部件发生故障
   + 分析此故障对系统功能造成的影响
   + 根据分析结果，判断架构是否需要进行优化
   
 * FMEA分析表
   1. 功能点
     + 指的是从用户角度来看的，而不是从系统各个模块功能点划分来看的
   2. 故障模式
     + 故障模式指的是系统会出现什么样的故障，包括故障点和故障形式
   3. 故障影响
     + 当发生故障模式中描述的故障时，功能点具体会受到什么影响
     + 常见的影响有：功能点偶尔不可用、功能点完全不可用、部分用户功能点不可用、功能点响应缓慢、功能点出错等
     + 故障影响也需要尽量准确描述。例如，推荐使用“20% 的用户无法登录”，而不是“大部分用户无法登录”
   4. 故障严重程度
     + 严重程度指站在业务的角度故障的影响程度，一般分为“致命 / 高 / 中 / 低 / 无”五个档次。
     + 严重程度按照这个公式进行评估：严重程度 = 功能点重要程度 × 故障影响范围 × 功能点受损程度。
   5. 故障原因
     + 不同的故障原因发生概率不相同
     + 不同的故障原因检测手段不一样
     + 不同的故障原因的处理措施不一样
   6. 故障出现概率
     + 一般分为“高 / 中 / 低”三档
     + 重点关注点
        - 硬件：硬件随着使用时间推移，故障概率会越来越高
        - 开源系统：成熟的开源系统 bug 率低，刚发布的开源系统 bug 率相比会高一些；自己已经有使用经验的开源系统 bug 率会低，刚开始尝试使用的开源系统 bug 率会高。
        - 自研系统
   7. 故障风险
     + 风险程度 = 严重程度 × 故障概率。
   8. 故障对应措施
     + 包括：检测告警、容错、自恢复等。
   9. 故障避免措施
     + 规避措施指为了降低故障发生概率而做的一些事情，可以是技术手段，也可以是管理手段。
   10. 故障解决措施
   11. 后续方案
   

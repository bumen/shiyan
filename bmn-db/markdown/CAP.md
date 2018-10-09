## CAP原则
 * 非关系型数据库，遵循CAP定理。
 * consistency
   + 一致性
 * availability
   + 可用性
 * partition-tolerance
   + 分区容错
### 一致性模型
 * 强一致性
 * 弱一致性
 * 最终一致性
   + 如何保证
     - 冗余数据全量定时扫描
     - 冗余数据增量日志扫描
     - 冗余数据线上消息实时检测
     
 * 因果一致性
 * 读你所写一致性
 * 会话一致性
 * 单调一致性
 * 单调写一致性
 
### Eurela与Zookeeper区别
 * Zookeeper 是CP
   + 一致性优先，可用性靠后
   + 只向Leader注册
   + 如果Leader挂了，通过在follower选举一个成为Leader（耗时不提供注册，此时不是高可用）,
   同时会把所其它follower同步一样后才能再提供注册功能（实现一致）
 * Eureka 是AP
   + 没有选举
   + 当刚一注册，这台Eureka就挂掉，此时其它Eureka还没有同步过来，造成不一致
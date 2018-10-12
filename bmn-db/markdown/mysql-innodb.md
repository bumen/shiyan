## Innodb引擎

### Innodb功能
 * 支持事物
 * 支持行级锁
 * 支付高并发读，通过MVCC(多版本并发控制 Multi Version Concurrency Control)
   + MVCC通过读取旧版本数据，来降低并发事务的锁冲突，提高任务的并发度
   
#### Innodb5.6新功能
 * 全文索引
 * alter table可以不拷贝表，且不阻塞写操作。并不所有表都这样
 * 建表时，允许一个table一个文件了
   + 可以实现热数据表放SSD，数据量大表放在HDD
 * 支持memcached插件
 * 可以支持只读实例
   + 把Innodb表放到DVD,方便共享
   + 多个实例共用一份数据
 
   
### 并发控制
 * 并发任务对同一个临界资源操作，如果不采取措施，可能导致不一致
 * 通过并发控制保证数据一致性常见手段
   + 锁
   + 数据多版本(MVCC)
 * 结论
   + 普通锁：串行执行
   + 读写锁：读读并发
   + 数据多版本：读写并发
 
#### 锁
 * 普通锁
   + 操作数据前，锁住，操作完成后释放锁
   + 锁住太过粗暴，连读任务也无法进行，任务执行过程是串行
   
 * 共享锁（读写锁）
   + 读时加锁，可以读读并行
 * 排它锁
   + 修改数据时加锁，写读，写写不可以并行
 * 一旦写数据没有完成，数据是不能被其它任务读取，这对并发度有较大影响
 对应数据库，可以理解为，写事物没有提交，读相关数据的select也会被阻塞
 
#### 数据多版本
 * 进一步提高并发的方法
 * 过程
   + 写任务发生时，将数据克隆一份，以版本号区分
   + 写任务操作新克隆的数据，直到提交
   + 并发读任务可以继续读取旧版本的数据，不至于阻塞
 * 旧版本数据存在哪
   + 旧版本数据存在回滚段
   
 * 快照读（Snapshot read），这种一致性不加锁的读，就是Innodb并发如此高的核心原因之一。
   + 普通select操作是快照读
   + 显示加锁，非快照读：select * from a lock in share mode; select * from a from update
   
### redo, undo, 回滚段
#### redo日志
 * 数据库事务提交后，必须将更新后的数据刷新到磁盘，以保证ACID特性。磁盘随机写性能较低，如果每次都刷盘，会极大影响数据库吞吐量。
 * 优化方式是：将修改行为先写到redo日志（此时变为顺序），再定期将数据刷到磁盘
   + 随机写优化为顺序写
 * 如果数据库崩溃，还没来得急刷盘，在数据库重启后，会重做redo日志里的内容，以保证已提交事务对数据产生影响都刷到磁盘。
 * redo日志用于保障，已提交事务的ACID特性
 
#### undo日志
 * 事务未提交时，会将事物修改的数据镜像（即修改前的旧版本）存放到undo日志，当事物回滚时，或数据库崩溃时可以利用undo日志
 撤消未提交的事务对数据库产生的影响。
 * 对insert操作，回滚时直接删除
 * 对del/update操作，回溯时直接恢复
 * undo日志用于保障，未提交事务不会对数据库的ACID特性产生影响
 
#### 回滚段
 * 存undo日志的地方，是回滚段
 
### Innodb锁
 * 记录锁（record lock）
   + 行锁是实现在索引上的，而不是锁在物理行记录上。如果访问没有命中索引，也无法使用行锁，将要退化为表锁
   + 当未命中索引，或在没有索引的字段查询时，退化为表锁
   
 * 共享/排它锁（Share and Exclusive locks）
   + 行级锁
 * 意向锁（intention lock）
   + 表级锁
 * 间隙锁（gap lock）
 * 临键锁（next-key lock）
 * 插入意向锁（insert intention lock）
 * 自增锁（auto-inc lock）
   + 特殊表级锁
   
#### 记录锁
 * 是select for update, select lock in share mode时使用的索引记录锁
 * 阻止插入，更新，删除单行记录
 * 间隙锁，临键锁类似，更强调的是一个范围
  
#### 自增锁
 * 是一种特殊的表级别锁，专门针对事务插入AUTO_INCREMENT类型的列
 * 如果一个事务正在表中插入记录，所有其它事务插入必须等待，以便第一个事务插入的行是连续的主键值。
#### 共享/排它锁（读写锁）
 * 事务拿到某一行记录的共享S锁，才可以读取这一行
 * 事务拿到某一行记录的排它X锁，才可以插入修改或删除这一行
 * 不能充分的并行，解决思路是数据多版本
 * 共享锁与共享锁不互斥
 * 排它锁与共享锁，排它锁互斥
 * 一般与记录锁，间隙所，临键锁组合使用
 
#### 意向锁
 * 是指：未来某个时刻，事务可能要加共享/排它锁了，先提前声明一个意向。
 * 表级锁
 * 分为：意向共享锁IS，意向排它锁IX
 * 意向锁协议并不复杂
   + 事务要获得某些行的S锁，必须先获取表的IS锁
   + 事务要获得某些行的X锁，必须先获取表的IX锁
 * 意向锁并不相互互斥
 
#### 插入意向锁
 * 区间锁
 * 对已有数据修改与删除，必须加强互斥X排它锁，那对于插入，是不是需要呢
 * 插入意向锁是间隙锁的一种（是实施在索引上的），它专门针对insert操作
 * 多个事务，在同一个索引，同一个范围区间插入记录时，如果插入的位置不冲突，不会阻塞彼此
 
#### 间隙锁
 * 区间锁
 * 防止其它事务在间隔中**插入**数据，以导致“不可重复读”
 * 如果把事务隔离级别降为读提交，间隙锁会自动失效
 如果失效时变成单条互斥，则delete不存在记录时，可以插入索引不冲突记录
 
#### 临键锁
 * 区间锁
 * 是记录锁与间隙锁的组合，它封锁范围，即包含索引记录，又包含索引区间。
 * 如果有一个会话占有了索引记录R的共享/排它锁，其它会话不能立刻在R之前的区间**插入**新的索引记录。
 * 主要目的，也是为了避免幻读，如果把事务隔离级别降到RC，临键锁也会失效。
 如果失效时变成单条互斥，则delete不存在记录时，可以插入索引不冲突记录
 
#### sql中锁的使用
 * insert，它会排它锁封锁被插入的索引记录，而不会封锁记录之前的范围,
 同时，会在插入区间插入意向锁，但这个不会真正封锁区间，也不会阻止相同区间的不同key插入。
 插入时，先会select判断插入的主键是否存在
 
 * delete 
   + 删除一条不存的记录，会获取共享锁+间隙锁。会阻止其他事务B在相应的区间插入数据。因为插入数据需要获取排它锁+间隙锁。
 * update/delete
   + 对有记录修改删除，如果是单条则获取记录锁，如果是区间需要获取排它锁+间隙锁
 
#### 死锁
 * 共享排它锁死锁
   + 多个事务获取共享锁，然后再去获取排它锁，导致死锁
   + 多个事务插入相同记录，有其中一个事务回滚了，导致死锁
 * 并发间隙锁死锁
   + 不好排查
   + 

### Innodb事务
 * Innodb使用不同锁策略来实现不同的隔离级别
 
 * 脏读
   + 事务A读到事务B未提交的数据
 * 不可重复读
   + 事务A第一次读后，事物B更新数据并提交，事务A再次读时与第一次读结果不一致
   + 同一事务不可以重复读，因为重复读出现数据不一致
 * 幻读
   + 事务A查询数据没有，事物B插入数据并提交，事务A此时要插入，发现插入不了主键冲突
   + 事物B影响了事务A
   
 * 读未提交
   + 并发性最好，会产生脏读
 * 读提交 RC
   + 解决脏读问题，保证读到的数据行都是已提交事务写入的。 
 * 可重复读
   + 解决不重复读
 * 串行
   + 普通select会被隐式转化为select in share mode, 加锁模式
   + 并发最差，当有未提交的事务正在修改某些行，则后续读取这些行的select操作需要阻塞
   + 解决幻读

#### Innodb事物隔离级别
 * 为什么要事物隔离级别
   + 解决高并发下，产生数据不一致问题
 * 
   
### Innodb索引

#### 索引结构
 * B+树
 * 在索引结构中，非叶子结点存储key, 叶子结点存储value

#### MyISAM索引
 * 索引与记录分开存储
   + 所以可以没有主键
 * 主键索引与普通索引没有本质区别
 * 有连续聚集的区域单独存储行记录
 * 主键索引
   + 叶子节点，存储主键，与对应记录的指针
 * 普通索引
   + 叶子节点，存储索引列，与对应记录的指针
   + 普通索引查询一次可以找到记录
   
 * 主键索引与普通索引是两个独立的索引B+树，通过先定位叶子节点，再定位到记录
 
 
#### Innodb聚集索引
 * 叶子节点存储行记录(row)
 * Innodb索引和记录是存储在一起的。没有单独区域存储行记录。而MyISAM是分开的
 * innodb每个表都有聚集索引，也只能有一个
 * 如果表定义了PK, 则PK就是聚集索引
 * 如果表没有定义PK, 则第一个非空unique列是聚集索引
 * 否则，会创建一个隐藏的row-id作为聚集索引

#### Innodb普通索引
 * 叶子节点存储了PK的值
 * 所以，Innodb普通索引会扫描两遍
 * 第一遍，由普通索引找到PK, 第二遍，由PK使用聚集索引找到row
 * 不建议使用较长的列做为主键，例如char(64), 因为所有的普通索引都会存储主键，会导致普通索引过于庞大
 * 建议使用趋势递增的key做为主键，由于数据行与索引一样，这样不至于插入记录时，有大量索引分裂，行记录移动。
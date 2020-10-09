### im mysql 优化
 * [mysql参数](https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-configuration-properties.html)

 * 打开慢查询日志
   + 这个如果在配置文件中没有打开，则需要设置 global variables打开
    
 * 查看慢查询日志
   + 切到root账号
   + mysqldumpslow -s t -t 10 db/v-im04-slow.log
   
 * explain 分析sql语句，索引相关优化
 * 打开profiling分析
   + 打开是当前会话variables
   1. 打开：mysql> set profiling=1;
   2. 执行: mysql> update t_user_session set `_dt` = 1598535792331, `_cid` = '14912' where `_uid` = '14912' limit 1;
   3. 查看：mysql> show profiles; 
   4. 分析：mysql> show profile for query 1; 
   
 * mysqladmin -u playcrab -p -i 3 -h10.2.24.167 -P51001 processlist;
   + 实时查看当前mysql语句执行状态耗时
 * mysqladmin -u playcrab -p -i 3 -h10.2.24.167 -P51001 status;
   + 实时查看当前mysql
   
### mysql 配置
 * 设置隔离级别
   + transaction_isolation = READ-COMMITTED
   
 * innodb_flush_log_at_trx_commit=0
   + 降低query end 耗时
   + 这个优化只是query end 一个方面，有的sql 可能还会query end 耗时很高：（如: update语句的索引创建不合理问题）
   
 * sync_binlog 参数优化
   + 当sync_binlog = N (N > 0)，mysql 在每写N次二进制binary log时，会使用fdatasync()函数将它的写二进制binary log同步到磁盘
   + 当N = 0时，mysql 不会同步到磁盘中去而是依赖操作系统刷新binary log
   + show global variables like "%binlog%";
   + 默认值：1
   + set global variables sync_binlog=500; 修改为500
   + 优化完之后发现，insert 插入慢查询减少很多
   
 * innodb_buffer_pool_size 
   + 提升读的原理：因为 buffer_pool_size 设置的比较大， 很多表数据和索引已缓存到 buffer pool , 要查询的数据在缓存中找到了，就不需要访问磁盘了。读性能就得到了提升。
   + 提升写的原理：因为 buffer_pool_size 设置的比较大， 写的数据，暂时以脏页的方式放在内存，然后慢慢落到磁盘，如果buffer_pool_size 太小就没办法缓存写操作，写一次访问一次磁盘 ，写入性能就比较慢
   + 通常将innodb_buffer_pool_size其配置为物理内存的50%到75%
   + 默认：134217728 = 128m
   + set global innodb_buffer_pool_size=8589934592;
     - 设置8G (物理内存是16G)
     - 如果表数据增长，需要再调整
   
   
  

#### t_user_session表优化
 * 压测发现大量route请求超时
   + route功能会更新session
 * 查看慢查询记录
   + 有1百多条，平均2秒左右，最大8秒
 ```
    * mysqldumpslow -s r -t 10 db/v-im04-slow.log -a
    Count: 1  Time=8.83s (8s)  Lock=0.00s (0s)  Rows=0.0 (0), playcrab[playcrab]@[10.2.24.168]
    update t_user_session set `_dt` = 1598535792331, `_cid` = '14912' where `_uid` = '14912'

    * mysqldumpslow -s t -t 10 db/v-im04-slow.log
    Count: 124  Time=2.39s (296s)  Lock=0.00s (0s)  Rows=0.0 (0), playcrab[playcrab]@[10.2.24.168]
    update t_user_session set `_dt` = N, `_cid` = 'S' where `_uid` = 'S'
 ```
 * 通过show profile分析
   + 发现query end操作比较耗时
     - query end 状态也可以通过show processlist;实时查看到当前线程正在执行的sql语句耗时状态，也发现query end状态耗时很长
 ``` 
    1. 执行sql 
    mysql> update t_user_session set `_dt` = 1598535792331, `_cid` = '14912' where `_uid` = '14912';
    2. 查看记录
    mysql> show profiles;
    +----------+------------+------------------------------------------------------------------------------------------+
    | Query_ID | Duration   | Query                                                                                    |
    +----------+------------+------------------------------------------------------------------------------------------+
    |        1 | 0.00488350 | show  variables like "%pro%"                                                             |
    |        2 | 0.00035400 | update t_user_session set `_dt` = 1598535792331, `_cid` = '14912' where `_uid` = '14912' |
    |        3 | 0.00043750 | SELECT DATABASE()                                                                        |
    |        4 | 0.00644100 | show databases                                                                           |
    |        5 | 0.01783900 | show tables                                                                              |
    |        6 | 0.02841075 | update t_user_session set `_dt` = 1598535792331, `_cid` = '14912' where `_uid` = '14912' |
    +----------+------------+------------------------------------------------------------------------------------------+
    3. 分析 
    mysql> show profile for query 6;
    +----------------------+----------+
    | Status               | Duration |
    +----------------------+----------+
    | starting             | 0.000142 |
    | checking permissions | 0.000015 |
    | Opening tables       | 0.000286 |
    | init                 | 0.000042 |
    | System lock          | 0.000158 |
    | updating             | 0.000993 |
    | end                  | 0.000019 |
    | query end            | 0.026524 |
    | closing tables       | 0.000059 |
    | freeing items        | 0.000094 |
    | cleaning up          | 0.000078 |
    +----------------------+----------+
    
 ```
 * 优化了innodb_flush_log_at_trx_commit=0后发现query end 还是没有降低
 * 查看表索引
   + 有两个唯一索引，同时cid+uid是一个组合唯索引
 ``` 
    mysql> show index from t_user_session;
    +----------------+------------+-----------------------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+
    | Table          | Non_unique | Key_name              | Seq_in_index | Column_name | Collation | Cardinality | Sub_part | Packed | Null | Index_type | Comment | Index_comment | Visible |
    +----------------+------------+-----------------------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+
    | t_user_session |          0 | PRIMARY               |            1 | id          | A         |      118357 |     NULL |   NULL |      | BTREE      |         |               | YES     |
    | t_user_session |          0 | session_uid_cid_index |            1 | _cid        | A         |      111469 |     NULL |   NULL |      | BTREE      |         |               | YES     |
    | t_user_session |          0 | session_uid_cid_index |            2 | _uid        | A         |      116941 |     NULL |   NULL |      | BTREE      |         |               | YES     |
    | t_user_session |          1 | session_token_index   |            1 | _token      | A         |           1 |     NULL |   NULL | YES  | BTREE      |         |               | YES     |
    | t_user_session |          1 | session_uid_index     |            1 | _uid        | A         |      112385 |     NULL |   NULL |      | BTREE      |         |               | YES     |
    +----------------+------------+-----------------------+--------------+-------------+-----------+-------------+----------+--------+------+------------+---------+---------------+---------+
 ```
 * 删除索引
   +  drop index session_uid_cid_index on t_user_session;
 * 查看profile
   + 发现query end耗时很低了
 ``` 
    mysql> show profiles;
    +----------+------------+--------------------------------------------------------------------------------------------------+
    | Query_ID | Duration   | Query                                                                                            |
    +----------+------------+--------------------------------------------------------------------------------------------------+
    |        1 | 0.00114650 | update t_user_session set `_dt` = 1598535792331, `_cid` = '14912' where `_uid` = '14912' limit 1 |
    +----------+------------+--------------------------------------------------------------------------------------------------+
    1 row in set, 1 warning (0.00 sec)
    
    mysql> show profile for query 1;
    +----------------------+----------+
    | Status               | Duration |
    +----------------------+----------+
    | starting             | 0.000285 |
    | checking permissions | 0.000026 |
    | Opening tables       | 0.000122 |
    | init                 | 0.000048 |
    | System lock          | 0.000173 |
    | updating             | 0.000247 |
    | end                  | 0.000024 |
    | query end            | 0.000055 |
    | closing tables       | 0.000029 |
    | freeing items        | 0.000046 |
    | cleaning up          | 0.000092 |
    +----------------------+----------+
 ```
 
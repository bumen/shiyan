### mysql 性能优化


### show query log
 * 配置
   + 查看是否开启慢查询日志记录  
   ``` 
    mysql> show variables like 'slow%';
    +---------------------+----------------------------------------------------------------+
    | Variable_name       | Value                                                          |
    +---------------------+----------------------------------------------------------------+
    | slow_launch_time    | 2                                                              |
    | slow_query_log      | ON                                                             |
    | slow_query_log_file | /data/home/user00/playcrab/mysql_work/51001/db/v-im04-slow.log |
    +---------------------+----------------------------------------------------------------+
    3 rows in set (0.00 sec)
   ```
   + 开启  
   ` set global slow_query_log='ON'`
   + 查看记录时间
   ``` 
   mysql>  show variables like 'long_query_time';
   +-----------------+----------+
   | Variable_name   | Value    |
   +-----------------+----------+
   | long_query_time | 1.000000 |
   +-----------------+----------+
   1 row in set (0.00 sec)
   ```
   + 设置记录时间  
   `set global long_query_time=1;`
   

 * 查看有多少条慢查询  
   `show global status like '%Slow_queries%';`
   
 * 分析slow.log
   + mysqldumpslow -s t -t 10 v-im04-slow.log
   
### show processlist 
 * 固定频率打印
 ``` 
    mysqladmin -u playcrab -p -i 1 -h10.2.24.167 -P51001 processlist
    +--------+-----------------+-------------------+--------+-------------+--------+---------------------------------------------------------------+------------------+
    | Id     | User            | Host              | db     | Command     | Time   | State                                                         | Info             |
    +--------+-----------------+-------------------+--------+-------------+--------+---------------------------------------------------------------+------------------+
    | 4      | event_scheduler | localhost         |        | Daemon      | 925376 | Waiting on empty queue                                        |                  |
    | 6567   | ms_mysql        | 10.2.24.168:39251 |        | Binlog Dump | 833920 | Master has sent all binlog to slave; waiting for more updates |                  |
    | 387797 | playcrab        | 10.2.24.168:35753 | imchat | Sleep       | 9      |                                                               |                  |
    | 387798 | playcrab        | 10.2.24.168:35755 | imchat | Sleep       | 9      |                                                               |                  |
    | 387799 | playcrab        | 10.2.24.168:35757 | imchat | Sleep       | 9      |                                                               |                  |
    | 387800 | playcrab        | 10.2.24.168:35759 | imchat | Sleep       | 9      |                                                               |                  |
    | 387801 | playcrab        | 10.2.24.168:35761 | imchat | Sleep       | 9      |                                                               |                  |
    | 387802 | playcrab        | 10.2.24.168:35763 | imchat | Sleep       | 9      |                                                               |                  |
    | 387803 | playcrab        | 10.2.24.168:35765 | imchat | Sleep       | 9      |                                                               |                  |
    | 387804 | playcrab        | 10.2.24.168:35767 | imchat | Sleep       | 9      |                                                               |                  |
    | 387805 | playcrab        | 10.2.24.168:35769 | imchat | Sleep       | 9      |                                                               |                  |
    | 387806 | playcrab        | 10.2.24.168:35771 | imchat | Sleep       | 9      |                                                               |                  |
    | 387807 | playcrab        | 10.2.24.167:10637 |        | Query       | 0      | starting                                                      | show processlist |
    +--------+-----------------+-------------------+--------+-------------+--------+---------------------------------------------------------------+------------------+
 ```
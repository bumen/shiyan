### ORDER BY详解
 * MySQL有两种方式可以实现
   1.通过索引扫描生成有序的结果
   2 索引扫描排序和文件排序(filesort)简介
   
 
 * 索引扫描排序执行过程分析
   + 介绍索引扫描排序之前，先看看索引的用途
   ``` 
    SQL语句中，WHERE子句和ORDER BY子句都可以使用索引：WHERE子句使用索引避免全表扫描，ORDER BY子句使用索引避免filesort
    （用“避免”可能有些欠妥，某些场景下全表扫描、filesort未必比走索引慢），以提高查询效率。
    虽然索引能提高查询效率，但在一条SQL里，对于一张表的查询 一次只能使用一个索引。
    也就是说当WHERE子句与ORDER BY子句要使用的索引不一致时，MySQL只能使用其中一个索引(B+树)。
    
    也就是说，一个既有WHERE又有ORDER BY的SQL中，使用索引有三个可能的场景：
     1. 只用于WHERE子句 筛选出满足条件的数据
     2. 只用于ORDER BY子句 返回排序后的结果
     3. 既用于WHERE又用于ORDER BY，筛选出满足条件的数据并返回排序后的结果
   ```
   + 如
   ``` 
       1. uid string类型 创建索引
       2. seq long类型 创建索引
       select * from tb where uid = 123 order by seq;
       执行过程
       1. 先通过uid索引找到所有满足WHERE条件的主键id（注:从b+树根节点往下找叶子节点，时间复杂度为O(logN))
       2. 再根据这些主键id去主键索引(聚簇索引))找到这几行的数据，生成一张临时表放入排序缓冲区（时间复杂度为O(M*logN)，M是临时表缓冲区里的行数）
       3. 对临时表缓冲区里的数据进行排序（时间复杂度O(M*logM)，M是临时表缓冲区里的行数)
       
       场景二 索引只用于ORDER BY子句
       // 强制使用seq索引
       select * from tb force index(seq) where uid = 123 order by seq;
       执行过程
       1. 从seq索引的第一个叶子节点出发，按顺序扫描所有叶子节点
       2. 根据每个叶子节点记录的主键id去主键索引(聚簇索引))找到真实的行数据，判断行数据是否满足WHERE子句的uid条件，若满足，则取出并返回
       
       场景三 索引既用于WHERE又用于ORDER BY
       select * from tb force index(seq) where uid = 123 order by uid;
   ```
   
 * 4 文件排序(filesort)
   + 如果需要排序的数据量小于“排序缓冲区”，MySQL使用内存进行“快速排序”操作。如果内存不够排序，那么MySQL会先将数据分块，
   可对每个独立的块使用“快速排序”进行排序，再将各个块的排序结果放到磁盘上，然后将各个排好序的块进行“归并排序”，最后返回排序结果。
   + 所以filesort是否会使用磁盘取决于它操作的数据量大小。
     1.数据量小时，在内存中快排
     2.数据量大时，在内存中分块快排，再在磁盘上将各个块做归并
   + 根据回表查询的次数，filesort又可以分为两种方式：
     1.回表读取两次数据(two-pass)：两次传输排序
     2.回表读取一次数据(single-pass)：单次传输排序
     
   + 两次传输排序
     ``` 
        1. 利用create_time索引，对满足WHERE子句create_time >= '2018-08-11 00:00:00' and create_time < '2018-08-12 00:00:00'的rowid进行回表（第一次回表），
        回表之后可以拿到该rowid对应的userid，若userid满足userid > 140000的条件时，则将该行的rowid，money(ORDER BY的列)放入排序缓冲区
        2. 若排序缓冲区能放下所有rowid, money对，则直接在排序缓冲区（内存）进行快排。
        3. 若排序缓冲区不能放下所有rowid, money对，则分块快排，将块存入临时文件（磁盘），再对块进行归并排序。
        4. 遍历排序后的结果，对每一个rowid按照排序后的顺序进行回表操作（第二次回表），取出SELECT子句需要的所有字段。
        熟悉计算机系统的人可以看出，第二次回表会表比第一次回表的效率低得多，因为第一次回表几乎是顺序I/O；而由于rowid是根据money进行排序的，
        第二次回表会按照rowid乱序去读取行记录，这些行记录在磁盘中的存储是分散的，每读一行 磁盘都可能会产生寻址时延（磁臂移动到指定磁道）+旋转时延（磁盘旋转到指定扇区），这即是随机I/O。
        
        所以为了避免第二次回表的随机I/O，MySQL在4.1之后做了一些改进：在第一次回表时就取出此次查询用到的所有列，供后续使用。我们称之为单次传输排序。
     ```
   + 单次传输排序（MySQL4.1之后引入）
     ``` 
        1. 利用create_time索引，对满足WHERE子句create_time >= '2018-08-11 00:00:00' and create_time < '2018-08-12 00:00:00'的rowid进行回表（第一次回表），回表之后可以拿到改rowid对应的userid，若userid满足
        userid > 140000的条件时，则将此次查询用到该行的所有列（包括ORDER BY列）取出作为一个数据元组(tuple)，放入排序缓冲区
        2. 若排序缓冲区能放下所有tuples，则直接在排序缓冲区（内存）进行快排。
        3. 若排序缓冲区不能放下所有tuples，则分块快排，将块存入临时文件（磁盘），再对块进行归并排序。
        4. 遍历排序后的每一个tuple，从tuple中取出SELECT子句需要所有字段。
        单次传输排序的弊端在于会将所有涉及到的列都放入排序缓冲区，排序缓冲区一次能放下的tuples更少了，进行归并排序的概率增大。列数据量越大，需要的归并路数更多，增加了额外的I/O开销。
        所以列数据量太大时，单次传输排序的效率可能还不如两次传输排序
        
        当然，列数据量太大的情况不是特别常见，所以MySQL的filesort会尽可能使用单次传输排序，但是为了防止上述情况发生，MySQL做了以下限制：
          1. 所有需要的列或ORDER BY的列只要是BLOB或者TEXT类型，则使用两次传输排序
          2. 所有需要的列和ORDER BY的列总大小超过max_length_for_sort_data字节，则使用两次传输排序
     ```
   
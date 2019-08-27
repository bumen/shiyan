## redis 原理 



### redis设计与实现.pdf
#### redis数据结构
 * SDS
   + 结构
   ``` 
    int free
    int len
    char buff[]
   ```
   + 有长度属性
   + 不会向C字符串出现内存益出与泄漏
   + 内存预分配（避免重复内存分配），惰性回收
   + 可以保存二进行数据，因为有长度。C字符串不能放带空字符的字符
   
 * list
   + 无环队列
   + 查头，尾数据O(1)
   + 查长度O(1)
   + 查前一个，后一个O(1)
   
 * dict
   + 结构
   ``` 
    dictht ht[2]
   ```
   + ht[0] 存放数据
   + ht[1], ht0需要rehash时用到ht[1]
   + rehash
     + ht[0].used *2
     + 将ht[0] 数据放到ht[1]. 
     + 将ht[1] 赋值给ht[0]
     + 清除ht[1]
     
   + 渐进式rehash
     + 如果ht[0] 数据太大，rehash时间长，所采用渐进式rehash
     + 在key 执行增，删，改，查时每次rehash一个ht[0]数据到ht[1] 直到所有数据完成
     + 查时需要先查ht[0], 再ht[1]
     + 增时只向ht[1]加
     
 * string
   + 当值为long 类型整数，encoding=int
   + 当值为long double 类型浮点数时，encoding=raw,embstr. 因为先将浮点数转为string 存。用时再转为浮点数
   + 当值为string类型时
     + 值长度大于32字节时，encoding=raw, 两次内存分配，两次回收，内存不连续
     + 值长度小于32字节时，encoding=embstr, 一次内存分配，一次回收，内存连续
     + embstr类型的字符串是只读的。如果要修改，则修改后encoding=raw
    
   + 优化
     + 尽量字符串小于32字节
     + 尽量使用整数
     
 * list
   + encoding=ziplist, linkedlist
   + ziplist使用同时满足条件
     - 列表对象保存的所有字符串元素长度都小于64字节
     - 元素数量小于512个
     - 其中一个不满足后，对使用linkedlist
     - 配置: list-max-ziplist-value, list-max-ziplist-entries
     
 * hash
   + encoding=ziplist, hashtable
   + ziplist存一个key,value。
     - 先将key放到尾，再将value放到尾。这样key,value是挨着的
     - 查找时会遍历链表
     - 与list满足条件一样，不满足时转为hashtable
     
 * set
   + encoding=intset, hashtable
   + intset使用条件
     - 保存的所有元素都是整数值
     - 元素数量不超过512个
     - 配置：set-max-intset-entries
     
 * sortedset
   + encoding=ziplist, skiplist
   + ziplist使用条件 
     - 元素数量小于128
     - 所有元素成员的长度都小于64字节
     - 配置：zset-max-ziplist-entries, zset-max-ziplist-value
 * redis 对象 
   + 属性
     - type：key的类型，string, list, hash, set, zset
     - encoding：使用的数据结构类型
     - ptr：指向数据结构指针
     - refcount：引用次数 
     - lru: 最后一次访问时间
 * redis共享 
   + 自动共享值为0-9999的字符串对象
   + set a 100
   + object refcount a
   
 * redis对象lru时间
   + 记录最后一次访问的时间
   + object ideatime key : 打印key的空转时间
     
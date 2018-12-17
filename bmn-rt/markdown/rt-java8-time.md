## time
 * java.time – 包含值对象的基础包
 * java.time.chrono – 提供对不同的日历系统的访问
 * java.time.format – 格式化和解析时间和日期
 * java.time.temporal – 包括底层框架和扩展特性
 * java.time.zone – 包含时区支持的类

### DateTimeFormatter
 * 是线程安全类，可以定义成static final
 * 格式化字符   
   > G 年代标志符  
   > y 年  
   > M 月
   > d 日  
   > h 时 (12小时制)  
   > H 时 (24小时制)  
   > m 分  
   > s 秒  
   > S 毫秒  
   > E 星期几  
   > D 一年中的第几天  
   > F 一月中第几个星期(以每个月1号为第一周,8号为第二周为标准计算)
   > w 一年中第几个星期
   > W 一月中第几个星期(不同于F的计算标准,是以星期为标准计算星期数,
   例如1号是星期三,是当月的第一周,那么5号为星期日就已经是当月的第二周了)
   > a 上午 / 下午 标记符
   > k 时 (24小时制,其值与H的不同点在于,当数值小于10时,前面不会有0)
   > K 时 (12小时值,其值与h的不同点在于,当数值小于10时,前面不会有0)
   > z 时区
 * DateTimeFormatter pattern = DateTimeFormatter.ofPattern("G yyyy年MM月dd号 E a hh时mm分ss秒");
 
### ChronoField枚举类
 * 配合java.time基本包中的类的get方法
 * 用于获取当前时间的某个值
 
### TemporalAdjusters
 * 配置java.time基本包中的类的with方法
 ```
    LocalDate now = LocalDate.now();
    //当前月份的第一天的日期,2017-03-01
    System.out.println(now.with(TemporalAdjusters.firstDayOfMonth())); 
    //下一个月的第一天的日期,2017-04-01
    System.out.println(now.with(TemporalAdjusters.firstDayOfNextMonth()));  
    //当前月份的最后一天,2017-03-31 --再也不用计算是28，29，30还是31
    System.out.println(now.with(TemporalAdjusters.lastDayOfMonth())); 
 ```
   
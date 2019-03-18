## shell
 * coolshell
 
### 可以处理多行数据
#### awk
 * 对多行的每一行可以截取出列
 * awk '{if($4 ~ /^.{2,3}$/) a[$4]++;} END {for(i in a) print i "," a[i];}' 201809-asr.txt > asr.txt
   ``` 
    2018-09-01 00:00:08.964 1535731008016337784sd112        速度
    2018-09-01 00:00:09.100 1535730953882093530sd112        你唱歌吧
    2018-09-01 00:00:09.145 1535731019294277526sd112        咱也
    2018-09-01 00:00:09.220 1535730987496976490sd112        一首歌叫
   ```
   + START: 表示开始前的处理
   + 每一行都会执行：取出第4列，if 条件正则匹配第4列只有2或3个字的字符串，统计相同串出现次数。
   + END：在统计完成后输出成“字符串，数量”格式，写到文件
   
 * 格式化输出
   + awk '{printf "\"%s\",\n", $1}' asr.txt
 * 倒数第几列
   + awk '{print $(NF-1)}'

#### sort
 * 对多行排序，可以按某一列排序
 * sort [-fbMnrtuk] [file or stdin]
   + -f  ：忽略大小写的差异，例如 A 与 a 视为编码相同；
   + -b  ：忽略最前面的空格符部分；
   + -M  ：以月份的名字来排序，例如 JAN, DEC 等等的排序方法；
   + -n  ：使用『纯数字』进行排序(默认是以文字型态来排序的)；
   + -r  ：反向排序；
   + -u  ：就是 uniq ，相同的数据中，仅出现一行代表；
   + -t  ：分隔符，默认  是用 [tab] 键来分隔；
   + -k  ：以那个区间 (field) 来进行排序的意思
 * sort -t ',' -k 2nr asr.txt > asr_sort.txt
   ``` 
    来转转,2
    来走,2
    来揍我,2
    来做呀,2
   ```
   + 用“,”号分隔，取第2列，按数字排序，反排序。结果写入asr_sort.txt
   
#### uniq
 * 对重复行去重。只能处理相邻行是重复的，所以一般与sort一起使用
 * uniq -c
   + 统计重复行出现次数
 * uniq -dc
   + 统计重复行出现次数，只显示重复行
 * uniq -u
   + 只显示不重复行

#### cut
 * cut -c 字符区间
   + -d  ：后面接分隔字符。与 -f 一起使用；
   + -f  ：依据 -d 的分隔字符将一段信息分割成为数段，用 -f 取出第几段的意思；
   1,7：取1列与7列
   1-7：取1到7列
   1-：取第1列之后所有
   + -c  ：以字符 (characters) 的单位取出固定字符区间；
   
 * 与awk区别，不同重新格式化输出结果
 
 * cut -d ':' -f 1,7 
   ``` 
    root:x:0:0:root:/root:/bin/bash
    bin:x:1:1:bin:/bin:/sbin/nologin
   ```
   + 已“:”分隔，取第1列与第7列
   ``` 
    root:/bin/bash
    bin:/sbin/nologin
   ```
   
#### xargs
 * xargs 可以读入stdin的内容，并且以空白字元或断行字元作为分隔，将stdin的内容分隔成为arguments。
 
 * -0 ：当sdtin含有特殊字元时候，将其当成一般字符，想/'空格等
 * -a file 从文件中读入作为sdtin
 * -n 
   + num 后面加次数，表示命令在执行的时候一次用的argument的个数，默认是用所有的
 * -p 
   + 操作具有可交互性，每次执行comand都交互式提示用户选择，当每次执行一个argument的时候询问一次用户
 * -t 表示先打印命令，然后再执行。
 * -i 或者是-I，这得看linux支持了
   + 将xargs的每项名称，一般是一行一行赋值给{}，可以用{}代替。
   + 如：ls *.txt |xargs -t -i mv {} {}.bak。
   把.txt文件重命名为.txt.bak
 * -r  no-run-if-empty 
   + 如果没有要处理的参数传递给xargsxargs 默认是带 空参数运行一次，
   如果你希望无参数时，停止 xargs，直接退出，使用 -r 选项即可，
   其可以防止xargs 后面命令带空参数运行报错。
 * -s num 
   + xargs后面那个命令的最大命令行字符数(含空格) 
 * -L  
   + 从标准输入一次读取num行送给Command命令 ，-l和-L功能一样
 * -d delim 
   + 分隔符，默认的xargs分隔符是回车，argument的分隔符是空格，这里修改的是xargs的分隔符
 * find 命令一起使用
   >管 道是把一个命令的输出传递给另一个命令作为输入，比如：command1 | command2但是command2仅仅把输出的内容作为输入参数。
   >find . -name "install.log" -print打印出的是install.log这个字符串，
   >如果仅仅使用管道，那么command2能够使用的仅仅是install.log这个字符串， 不能把它当作文件来进行处理。
   + 如：find . -name "install.log" -print | cat
   打印的是文件名
   + 如：find . -name "install.log" -print | xargs cat
   打印的是文件内容
 
### sed 向文件添加删除某行
 * sed -i '/^,N.t,F$/d' s2.txt
   + 删除文件中所有“,N.t,F”的字符串行
   
### top命令
 * cpu状态
   + 6.7% us — 用户空间占用CPU的百分比。
   + 0.4% sy — 内核空间占用CPU的百分比。
   + 0.0% ni — 改变过优先级的进程占用CPU的百分比
   + 92.9% id — 空闲CPU百分比
   + 0.0% wa — IO等待占用CPU的百分比
   + 0.0% hi — 硬中断（Hardware IRQ）占用CPU的百分比
   + 0.0% si — 软中断（Software Interrupts）占用CPU的百分比
   + 内存状态
   + 8306544k total — 物理内存总量（8GB）
   + 7775876k used — 使用中的内存总量（7.7GB）
   + 530668k free — 空闲内存总量（530M）
   + 79236k buffers — 缓存的内存量 （79M）
   + 各进程（任务）的状态监控
   + PID — 进程id
   + USER — 进程所有者
   + PR — 进程优先级
   + NI — nice值。负值表示高优先级，正值表示低优先级
   + VIRT — 进程使用的虚拟内存总量，单位kb。VIRT=SWAP+RES
   + RES — 进程使用的、未被换出的物理内存大小，单位kb。RES=CODE+DATA
   + SHR — 共享内存大小，单位kb
   + S — 进程状态。D=不可中断的睡眠状态 R=运行 S=睡眠 T=跟踪/停止 Z=僵尸进程
   + %CPU — 上次更新到现在的CPU时间占用百分比
   + %MEM — 进程使用的物理内存百分比
   + TIME+ — 进程使用的CPU时间总计，单位1/100秒
   + COMMAND — 进程名称（命令名/命令行）
   
### nload 查询出口入口流量
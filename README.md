## 堡门
 
### http

### lang

### socket


### gitignore
1. 以“/”开头表示目录；
2. 以“?”通配单个字符
3. 以“*”通配多个字符；
4. 以方括号“[]”包含单个字符的匹配列表；
5. 以叹号“!”跟踪某个文件或目录；

``` 
经测试发现，若要忽略一个文件夹下的部分文件夹，应该一个一个的标示。可能有更好的方法。
若test下有多个文件和文件夹。若要ignore某些文件夹，应该这个配置.gitignore文件。若test下有test1，test2,test3文件。要track test3，则.gitignore文件为：
test/test1
test/test2
!test/test3
若为：
test/
!test/test3 ，则不能track test3。
Git 中的文件忽略
1. 共享式忽略新建 .gitignore 文件，放在工程目录任意位置即可。.gitignore 文件可以忽略自己。忽略的文件，只针对未跟踪文件有效，对已加入版本库的文件无效。
2. 独享式忽略针对具体版本库 ：.git/info/exclude针对本地全局：  git config --global core.excludefile ~/.gitignore
忽略的语法规则：
(#)表示注释
(*)  表示任意多个字符; 
(?) 代表一个字符;
 ([abc]) 代表可选字符范围
如果名称最前面是路径分隔符 (/) ，表示忽略的该文件在此目录下。
如果名称的最后面是 (/) ，表示忽略整个目录，但同名文件不忽略。
通过在名称前面加 (!) ，代表不忽略。
例子如下：
# 这行是注释
*.a                   # 忽略所有 .a 伟扩展名的文件
!lib.a                # 但是 lib.a 不忽略，即时之前设置了忽略所有的 .a
/TODO            # 只忽略此目录下 TODO 文件，子目录的 TODO 不忽略 
build/               # 忽略所有的 build/ 目录下文件
doc/*.txt           # 忽略如 doc/notes.txt, 但是不忽略如 doc/server/arch.txt

```
 * 假设想要忽略根目录下所有的名为Win7Release或release的文件夹，可在.gitignore文件中添加如下规则
    + *[Rr]elease/
 * 当添加的规则中包含目录层级时，该目录应为相对于根目录的完整路径
    + */**/a/
 * /表示当前gitignore所在根目录
 * target/
    + 忽略目录target下的全部内容；注意，不管是根目录下的 /target/ 目录，
    还是某个子目录 /lib/target/ 目录，都会被忽略；
 * /upload/
    + 忽略根目录下的 /upload/ 目录的全部内容；
 * configuration/**/*-baw.properties
    + 忽略configuration目录下不同子目录（多级）下的baw.properties结尾的文件


### for 
 *  they check their local list for registered members
 * When a new member is registered for a topic
 * Hazelcast Topic uses the MessageListener interface to listen for events 
 * Hazelcast provides a distribution mechanism for publishing messages
 * it is actually registering for messages published by any member in the cluster
 
### of
 * returns the total number of published and received messages 
 * since the start of this member,
 * Each cluster member has a list of all registrations in the cluster
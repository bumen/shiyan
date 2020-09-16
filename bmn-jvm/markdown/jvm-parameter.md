## jvm 参数

### 标准参数（-）
 * 所有的JVM实现都必须实现这些参数的功能，而且向后兼容
 * 通过 -D<名称>=<值>，设置系统参数

### 非标准参数（-X）
 * 默认jvm实现这些参数的功能，但是并不保证所有jvm实现都满足，且不保证向后兼容；
 * -Xms: memory startup
 * -Xmx: memory maximum
 * -Xmn: memory nursery/new : 堆中新生代初始及最大大小（NewSize和MaxNewSize为其细化）
 * -Xss: stack size 栈大小
 * -Xoss: 本地方法栈大小，在hotspot虚拟机中无效，因为hotspot中不区分虚拟机栈与本地方法栈
 * -Xnoclassgc： 关闭class回收
 
 


### 非Stable参数（-XX）
 * 高级选项，高级特性，但属于不稳定的选项。
 * 此类参数各个jvm实现会有所不同，将来可能会随时取消，需要慎重使用（但是，这些参数往往是非常有用的）
 * -XX:PermSize -XX:MaxPermSize限制方法区大小
 * -XX:MaxDirectMemorySize 设置直接内存大小
 * -XX:+PrintGCApplicationStoppedTime
   + 它就会把JVM的停顿时间（不只是GC），打印在GC日志里。
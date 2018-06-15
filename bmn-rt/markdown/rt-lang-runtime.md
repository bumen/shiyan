## Runtime

### exit
 * 0 为正常退出， 非0为不正常退出
 * 退出两个阶段
   1. 执行shutdownHook
   2. 执行finalizer, 通过runFinalizersOnExit配置是否启动。 默认不启动。已经deprecated
   
   
### addShutdownHook
 * Java virtual machine响应退出事件 
   1. 程序正常退出，即最后一个非守护线程退出，或执行exit方法退出
   2. 响应用户中断，如：^C。 或系统事件， 如：用户注销，关机。
   
 * Hook是一个简单初始化，但为启动的Thread
 * 一但Hook sequence 启动，只能通过 halt方法停止
 * 一但Hook sequence 启动，如果再添加Hook, 或删除已经添加的Hook，将抛出异常
 * 写个Hook注意
   1. 注意线程安全
   2. 死锁  
   3. 要快速执行完成 ， 如：当关机，可能系统只有很短时间留给退出。
   
 * 一但Hook 抛出未捕获异常，与其它线程一样。执行ThreadGroup.uncaughtException， 同时停止线程。
   + 不会引起，virtual machine exit or halt. 
   
 * 罕见情况的中止 abort
   + 停止virtual machine 运行， 但不执行清理操作。
   + 如：
     1. unix 系统的sigkill
     2. win 系统的 TerminateProcess
     3. native 方法出错。如: 破坏内部数据结构，访问不存的内存。
   + 当 abort 时，不能保证 Hook被执行
   
### halt， 强制停止
 * 0 为正常， 非0为不正常退出
 * 当exit执行时，再执行halt， halt状态码会替换exit的状态码
 * 执行
   1. 当Hook sequence未执行，则就不执行
   2. 当Hook sequence执行， 则停止执行
   
### exec 执行系统命令
 * 是ProcessBuilder的简化实现
 
### load
 * 加载native文件，需要绝对路径
   + 直接查找绝对路径文件，不使用java.library.path或sun.boot.library.path查找
 * ClassLoader.loadLibrary实现

### loadLibrary
 * 加载native文件，只要文件名
   + 没有绝对路径，如果ClassLoader子类实现findLibrary方法，则在自定义路径查找
   + 否则只能在java.library.path或sun.boot.library.path中查找
 * ClassLoader.loadLibrary实现
 * 如果native方法，再某个类中使用了
   + 这个类需要使用静态初始化加载native 文件   
   `static { System.loadLibrary("LibFile"); }`
   + 当这个类初始化时， native code 也同时被加载
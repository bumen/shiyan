## NioEventLoopGroup

### 创建对象
 * 线程池大小（固定大小），默认cpu核数*2。最小为1
 * 线程工厂：LoopGroup中的所有EventExecutor都使用相同线程工厂
 * SelectorProvider
 * SelectStrategyFactory。 nio心跳策略。默认是有任务就马上select，否则阻塞方式select
 * 任务饱和策略
 * Executor：会传递到NioEventLoop中，真正线程池。默认为单线程池，即每次调用都会分配一个新的线程
 * EventExecutorChooserFactory：任务如何分配到线程的分配策略
 * 创建EventExecutor对象。是一个单线程实现方法。
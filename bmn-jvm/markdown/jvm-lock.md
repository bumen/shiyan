## 锁

### 自旋锁
 * 自旋等待，避免线程切换开销
 * 自旋等待本身虽然避免线程切换开销，但占用处理器时间，所以如果锁被占用的时间短，自旋等待效果就会非常好，反之白白消耗处理器资源。
 * 通过设置自旋等待时间
 
### 自适应自旋锁
 * 通过上次获取锁的等待情况，来动态调整这个自旋等待的时间
 
### 锁消除
 * JIT在运行时，对一些代码上要求同步，但是被检测到不可能存共享数据竞争的锁进行消除
 
### 锁粗化
 * 如果一系列操作都对同一个对象反复加锁和解锁，甚至加锁操作是出现在循环体中，那即使没有线程竞争，频繁地进行互斥同步操作也会导致不必要的性能损耗。
 * 把锁粗化到循环外
 
### 轻量级锁
 * 在没有多线程竞争的前提下，减少传统的重量级锁使用操作系统互斥量产生的性能消耗。
 * 加锁
   + 通过CAS操作设置对象头轻量级标志，成功则获取锁。失败则转为重量级锁，并阻塞等待
 * 减锁
   + 通过CAS操作清除对象头轻量级标志，成功则释放。失败则释放锁同时，唤醒被挂起的线程。
 * 如果没有竞争的情况下使用CAS操作避免了使用互斥量的开销。
 * 如果存在竞争的情况下除了互斥量开锁外，还额外发生了CAS操作。
 * 因此存在竞争的情况下轻量级锁比传统重量级锁更慢。
 
### 偏向锁
 * 在没有竞争的情况下把整个同步都消除掉，连CAS操作都不做。
 * 偏向第一个获取它的线程，如果接下来的执行过程中，该锁没有被其它线程获取，则持有偏向锁的线程将永远不需要同步
 * 当有另一个线程获取这个锁时，偏向模式结束。根据当前被锁定状态，撤销偏向后恢复到未锁定或轻量锁状态。
 * 后续操作同轻量级锁一样  
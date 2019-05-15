## mina IoBuffer
 
### SimpleIoBuffer
 * 简单实现低层还是java.nio.ByteBuffer

### CacheIoBuffer
 * 也是对java.nio.ByteBuffer合成关系实现。不同线程分别有自己的缓冲池大小
 
### 与netty区别
 * 没有使用堆外内存
 * 没有预分配机制
 
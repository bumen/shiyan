## java nio
 * http://stenographist11.rssing.com/chan-7008770/all_p805.html

### socket nio 
 * ServerSocketChannel配置非阻塞
 * Selector注册channel后，成为异步非阻塞
   + 是事件驱动
 
#### SocketChannel read
 * 非阻塞
 * 返回值
   + 大于0：读到字节
   + 等于0：没有读到字节，非阻塞正常
   + 等于-1：链路已经关闭，需要关闭SocketChannel，释放资源 
   
#### SocketChannel write
 * 非阻塞
 * 写一次并不能保证一次能把需要发送的字节数组发送完。
 此时出现写半包，我们需要注册写操作，不断轮询Selector将没有发送完的数据发送完成
 
 
### selector
 * selector都是通过操作key。来通知channel
 * selector.keys()
    + 已经注册的Key的集合，并不所有注册过的key都是仍然有效的
    + 不能直接修改，否则异常。
 * selector.selectedKeys();
    + 是已注册Key的集合的子集。
    + 集合中的每一个key都是选择器判断为已经准备好的，并且包含于key集合中的操作。
    + 不要与key的ready集合混淆。
    + 可以删除key，但不能向集合中添加
    + 一但一个选择器将一个key添加到已选择的集合中，选择器就不会移除这个key.
    + 将一个不存在selected集合中的key，添加到selected集合中时，key的ready集合会被设置
    + 所以想重置一个Key的ready集合，需要将这个key从selected集合中删除。
 * selector.cancelledKeys 集合
    + 已取消的key的集合，是选择器私有的。
    + 是已注册的key的集合的子集，这个集体包含了key.cancel()方法被调用过的key.这个key已经是无效的了，但还没有被注销。
 * selector.wakeup()
    + 唤醒select()操作, select可能返回0， 是一种select优雅退出方式。
    + 是非常耗性能的操作。netty中通过设置一个wakeup原子变量，避免多次调用
 * selector.close()
    + 选择器关闭时，所有被注册到该选择器上的channel都将被注销，并且相关的key将立即失效。
    + 唤醒select()操作，并关闭选择器, 相关联的key设置为无效
    + 遍历所有key
    + 执行deregister(key)
    + 此时还没有从keys，selectedKeys中删除key，但是key已经是无效的。
 * selector.deregister(key)
    + 执行channel.removeKey()
 * selector.select(), select(timeout), selectNow()
    + 已取消的key集合将会被检查。如果集合有Key，则每一个被取消的key将从另外两个集合中移除，并且相关联channel被注销。最后已取消集合被清空。
    + 已注册的key的集合中的key的interest集合将被检查，这步执行完后，对interest集合的修改不会影响剩余检查过程。
       - 一旦就绪条件被确定下来，底层操作系统就会进行查询，直到系统调用完成为止（有就绪条件就绪），select会阻塞。
       - 一但有条件就绪，对于就绪的channel，将执行以下两种操作中的一种：
       (1)如果channel的key还没有处于已选择的key的集合中，那么key的ready集合将被清空。然后表示操作系统发现的当前channel已经就绪好的操作的比特码将被设置。
       (2)否则，key在已选择的key的集合中，key的ready集合将被操作系统发现的当前channel已经就绪好的操作的比特码更新。
       所有之前的已经不再是就绪状态的操作不会被清除。因为是按位分离的。
    + 步骤2可能会花费很长时间（因为select阻塞），这个期间可能与该选择器关联的key可能会同时被取消。
       - 当步骤2结束时，步骤1将重新执行，以完成任意一个在选择进行过程中，key已经被取消的channel的注销。
    + select操作返回值是key的ready集合在步骤2中被修改的key的数量，而不是已选择的key集合中的通道总数。
       - 即从上一个select()调用之后进入就绪状态的channel数量。
       - 对于上次就绪的，本次还就绪或不就绪的不计数。这些可能还在已选择的key的集合中。
    + 会清除wakeup状态
    + 当wakeup在select之前被设置时，当调用select后会直接返回0
    + 遍历所有cancelSet中的key
    + 从keys，selectedKeys中删除key
    + 执行deregister(key)
    
 * channel.removeKey()
    + 解除channel中关联的key。channel为Registered（一个channel可以注册到多个selector上，只有所有的key都被remove后，registered=false）
    + 同时执行selectionKey.invalidate();
 * channel.close()
    + 遍历所有key然后
    + 执行selectionKey.cancel();(此时还没有删除key。isRegistered为true)
    + open = false
 * selectionKey.cancel()
    + 执行selectionKey.invalidate();
    + 同时key被注册到selector的cancelSet中
    + channel可能还是registered状态，但是无效
    
### channel 
 * channel的open标志，只有在下面两种情况下才会将open置为false。
    + 调用了channel.close()方法;
    + 或者操作channel读/写的当前线程发生中断时。
 * 不能把关闭 channel，注册到 selector上。否则将抛出异常。（netty中体现在rebuild时，会捕获异常）
 * channel在注册到selector之前，必须设成非阻塞的。c1.configureBlocking(false);一但被注册，就不能再设为阻塞的了。
 * channel.validOps()
    + 获取channel所支持的就都状态。如SocketChannel不支持--Accpet操作
 * 而且，客户端，channel关闭，不影响服务器端channel状态，但是服务器端channel关闭，影响客户端
 
### SelectionKey
 * key.attach(new Object()); Object o = key.attachment();
    + 设置一个关联对像， 等同，channel.register(s,i, new Object);
 * key.cancel();
    + 终结channel与选择器之间的注册关系。
    + 取消键，不能把一个channel注册到一个相关的键已经被取消的选择器上，否则异常。通常要先检查key的状态
    + 当被取消时，它将被放在相关的选择器的已取消的key集合里。注册不立即被取消，但key会立即失效。
       - 当再次调用select方法时，（或者一个正在进行的select调用结束），已取消的key的集合中的被取消的key将被清理掉，并且相应的注销也将完成。
       - channel会被注销。
       - 此时，key已经不存大了，但是channel还是connected, 只有调用channel的close()方法，才关闭channel 。 现在只不过是channel与selector注册关系不存在了
 * key.isValid()
    + 检查它是否仍然表示一种有效的注册关系。(key一但cancel，则就失效)
       
 * key.interestOps(1);
    + 改变interest集合，也可以使用channel.register重新注册来改变。两种方式是一样的
    + 当select正在进行时，改变interest不会影响正在进行的select操作。所有更改将会在下一次select体现出来。
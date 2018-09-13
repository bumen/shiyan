package com.bmn.socket.nio;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * Created by g on 2016/6/1.
 */
public class NioSelector {


    public static void main(String[] args) throws IOException {


        Socket socket = new Socket("", 8080);


    }

    public void selector() throws IOException {
        Selector selector = Selector.open();

/**
 * 1.已经注册的Key的集合，并不所有注册过的key都是仍然有效的。
 * 2.不能直接修改，否则异常。
 */
        Set<SelectionKey> keys = selector.keys();
/**
 * 1.是已注册Key的集合的子集。
 * 2.集合中的每一个key都是选择器判断为已经准备好的，并且包含于key集合中的操作。
 * 3.不要与key的ready集合混淆。
 * 4.可以删除key，但不能向集合中添加
 * 5.一但一个选择器将一个key添加到已选择的集合中，选择器就不会移除这个key.
 *   将一个不存在selected集合中的key，添加到selected集合中时，key的ready集合会被设置
 *   所以想重置一个Key的ready集合，需要将这个key从selected集合中删除。
 */
        Set<SelectionKey> selectedKeys = selector.selectedKeys();

/**
 * 已取消的key的集合，是选择器私有的。
 * 1. 是已注册的key的集合的子集，这个集体包含了key.cancel()方法被调用过的key.这个key已经是无效的了，但还没有被注销。
 *
 */


        /**
         * 三种选择调用。不管是哪一种被调用，下面步骤将被执行：
         * 1. 已取消的key集合将会被检查。如果集合有Key，则每一个被取消的key将从另外两个集合中移除，并且相关联channel被注销。最后已取消集合被清空。
         * 2. 已注册的key的集合中的key的interest集合将被检查，这步执行完后，对interest集合的修改不会影响剩余检查过程。
         *    一旦就绪条件被确定下来，底层操作系统就会进行查询，直到系统调用完成为止（有就绪条件就绪），select会阻塞。
         *    一但有条件就绪，对于就绪的channel，将执行以下两种操作中的一种：
         *      1. 如果channel的key还没有处于已选择的key的集合中，那么key的ready集合将被清空。然后表示操作系统发现的当前channel已经就绪好的操作的比特码将被设置。
         *      2. 否则，key在已选择的key的集合中，key的ready集合将被操作系统发现的当前channel已经就绪好的操作的比特码更新。
         *         所有之前的已经不再是就绪状态的操作不会被清除。因为是按位分离的。
         * 3. 步骤2可能会花费很长时间（因为select阻塞），这个期间可能与该选择器关联的key可能会同时被取消。
         *    当步骤2结束时，步骤1将重新执行，以完成任意一个在选择进行过程中，key已经被取消的channel的注销。
         *
         * 4. select操作返回值是key的ready集合在步骤2中被修改的key的数量，而不是已选择的key集合中的通道总数。
         *    即从上一个select()调用之后进入就绪状态的channel数量。
         *    对于上次就绪的，本次还就绪或不就绪的不计数。这些可能还在已选择的key的集合中。
         *
         * --对于通过已取消的key的集合还延迟注销，为了防止与正在进行的选择操作冲突的优化。在选择操作之前与之后立即注销channel
         */
        selector.select();      //一直阻塞，
        selector.select(1000);  //阻塞1000秒，后返回
        selector.selectNow();   //非阻塞，立刻返回

        selector.wakeup();  //唤醒select()操作, select可能返回0， 是一种select优雅退出方式。
/**
 * 1. 选择器关闭时，所有被注册到该选择器上的channel都将被注销，并且相关的key将立即失效。
 * 2. 唤醒select()操作，并关闭选择器, 相关联的key设置为无效
 */
        selector.close();
        Thread.currentThread().interrupt();     //中断选择器线程，并抛出InterruptException，同时调用wakup()，唤醒select()操作。

        selector.isOpen(); //判断selector是否可用
    }

    public void selectableChannel() throws IOException {
        SocketChannel c1 = SocketChannel.open();
/**
 * 1、不能把关闭 channel，注册到 selector上。否则将抛出异常。
 * 2、channel在注册到selector之前，必须设成非阻塞的。c1.configureBlocking(false);一但被注册，就不能再设为阻塞的了。
 */

        c1.validOps();  //获取channel所支持的就都状态。如果SocketChannel不支持--Accpet操作

/**
 * 1. channel关闭时，相关的Key会自动取消
 */
        c1.close();

        c1.configureBlocking(false);
        c1.isBlocking();
        c1.isConnected();
        c1.blockingLock();


        c1.finishConnect();
        c1.close();

        c1.isRegistered();      //判断channel是否注册到一个选择器上。在key取消后，一直到channel被注销为止。

        c1.keyFor(null);        //返回该channel和指定的选择器相关的key.


    }

    /**
     * 一个SelectionKey表示特定channel与一个选择器对象之间的注册关系
     *
     * 1. 对于一个给定的选择器和一个给定的channel,只有一种注册关系是有效的。但一个channel可以注册到多个选择器上。
     *    更新interest时，只是修改key。
     */
    public void selectionKey() {
        SelectionKey key = null;

        key.attach(new Object());   //设置一个关联对像， 等同，channel.register(s,i, new Object);
        Object o = key.attachment();    //返回设置的关联对象

        key.channel();      //获取关联通道
        key.selector();     //获取关联选择器

/**
 * 1.终结channel与选择器之间的注册关系。
 * 2.取消键，不能把一个channel注册到一个相关的键已经被取消的选择器上，否则异常。通常要李检查key的状态
 * 3.当被取消时，它将被放在相关的选择器的已取消的key集合里。注册不立即被取消，但key会立即失效。
 *   当再次调用select方法时，（或者一个正在进行的select调用结束），已取消的key的集合中的被取消的key将被清理掉，并且相应的注销也将完成。
 *   channel会被注销。
 *
 *   此时，key已经不存大了，但是channel还是connected, 只有调用channel的close()方法，才关闭channel 。 现在只不过是channel与selector注册关系不存在了
 *
 *   而且，客户端，channel关闭，不影响服务器端channel状态，但是服务器端channel关闭，影响客户端
 */
        key.cancel();       //
        //key取消的时候，可能channel的状态还是注册状态，一直到下一次操作发生。
        //
        //

        key.isValid();      //检查它是否仍然表示一种有效的注册关系。(key一但cancel，则就失效)

        //表示channel与选择器组合体所关心的操作
        key.interestOps();
/**
 * 1. 改变interest集合，也可以使用channel.register重新注册来改变。两种方式是一样的
 * 2. 当select正在进行时，改变interest不会影响正在进行的select操作。所有更改将会在下一次select体现出来。
 */
        key.interestOps(1);
//表示channel准备好要执行的操作，即相关channel已经就绪的操作。但SelectionKey不能改变ready集合
        key.readyOps();

    }
}

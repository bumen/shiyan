package com.bmn.rt.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 * Created by Administrator on 2017/6/8.
 */
public class MultipleSelector {

    public static void main(String[] args) throws IOException, InterruptedException {

        final Selector selector1 = Selector.open();

        final Selector selector2 = Selector.open();


        final SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress("localhost", 8888));

        System.out.println("is connected " + channel.isConnected());
        channel.finishConnect();


        channel.register(selector1, SelectionKey.OP_READ);
        channel.register(selector2, SelectionKey.OP_READ);


        final CountDownLatch latch = new CountDownLatch(2);

        Thread s1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("start select1");
                try {
                    int r =  selector1.select();
                    System.out.println(r);
                    if(r > 0) {
                        Set<SelectionKey> keys = selector1.selectedKeys();
                        Iterator<SelectionKey> it = keys.iterator();
                        while(it.hasNext()) {
                            SelectionKey key = it.next();
                            if(key.isReadable()) {
                                ByteBuffer buf = ByteBuffer.allocate(10);
                                channel.read(buf);

                                buf.flip();

                                byte[] b = buf.array();
                                String v =  new String(b);

                                System.out.println(v);
                            }
                        }
                    }
                    latch.countDown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("start select1 end");
            }
        });
        s1.start();

        Thread s2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("start select2");
                try {
                    int r = selector2.select();
                    System.out.println(r);
                    if(r > 0) {
                        Set<SelectionKey> keys = selector2.selectedKeys();
                        Iterator<SelectionKey> it = keys.iterator();
                        while(it.hasNext()) {
                            SelectionKey key = it.next();
                            if(key.isReadable()) {
                                ByteBuffer buf = ByteBuffer.allocate(10);
                                int n = channel.read(buf);
                                System.out.println(n);

                                buf.flip();

                                byte[] b = buf.array();
                                String v =  new String(b);

                                System.out.println(v);
                            }
                        }
                    }
                    latch.countDown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("start select1 end");
            }
        });
        s2.start();

        latch.await();
        System.out.println("client end");

    }
}

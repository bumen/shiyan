package com.bmn.rt.nio;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by Administrator on 2017/6/8.
 */
public class MultipleSelctorServer {
    public static void main(String[] args) throws IOException, InterruptedException {

        ServerSocket socket = ServerSocketFactory.getDefault().createServerSocket();

        socket.bind(new InetSocketAddress("localhost", 8888));

        System.out.println("server start");
        Socket c = socket.accept();
        System.out.println("server accept");

        byte[] buf = "abcd".getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(buf.length);
        buffer.put(buf);
        buffer.flip();

        c.getOutputStream().write(buf);
        c.getOutputStream().flush();
        Thread.sleep(3000);
        c.close();

        socket.close();
        System.out.println("server write");

    }
}

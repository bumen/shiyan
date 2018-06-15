package com.bmn.socket.netty.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Administrator on 2017/3/21.
 */
public class ChannelTest {

    public static void main(String[] args) throws IOException {
        String p = System.getProperty("user.dir");
        FileInputStream fin = new FileInputStream(p + "/netty/resources/t.txt");

        // 获取通道
        FileChannel fc = fin.getChannel();

        // 创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 读取数据到缓冲区

        int a = fc.read(buffer);
        System.out.println(a);

        int c = fc.read(buffer);
        System.out.println(c);
        buffer.flip();

        while (buffer.remaining()>0) {
            byte b = buffer.get();
            System.out.print(((char)b));
        }

        fin.close();

        writeT(buffer);
    }

    private static void writeT(ByteBuffer buf) throws IOException {
        String p = System.getProperty("user.dir") + "/netty/resources/w.txt";
        FileOutputStream out = new FileOutputStream(p);

        FileChannel channel = out.getChannel();

        buf.flip();

        channel.write(buf);

        out.close();

    }

}

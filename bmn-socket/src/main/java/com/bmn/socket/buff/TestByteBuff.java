package com.bmn.socket.buff;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @author: zyq
 * @date: 2018/11/30
 */
public class TestByteBuff {

    public static void main(String[] args) {
        ByteBuffer byteBuffer;

        CharBuffer charBuffer;

        ShortBuffer shortBuffer;
        IntBuffer intBuffer;

        FloatBuffer floatBuffer;

        LongBuffer longBuffer;


        ByteBuf data = ByteBufAllocator.DEFAULT.ioBuffer(10);

        long x = 1739461754000L;

        data.writeIntLE((int) x);

        long y = -808 & 0xFFFFFFFFL;
        long z = -808 & 0xFFFFFFFF;

        System.out.println( x + " to " + y + " same " + z);


        long a1 = -808;
        int a2 = (int) a1;

        System.out.println(a1 +" -- " + a2 + "--" + (a2 & 0xFFFFFFFFL));
    }
}

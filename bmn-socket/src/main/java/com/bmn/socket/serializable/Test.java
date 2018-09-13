package com.bmn.socket.serializable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class Test {


    public static void main(String[] args) throws IOException {
        ObjectVO vo = new ObjectVO();
        vo.setId(123);
        vo.setName("zhangsan");

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
        objectOutputStream.writeObject(vo);
        objectOutputStream.flush();
        objectOutputStream.close();

        byte[] b = os.toByteArray();

        System.out.println("The JDK serializable length is : " + b.length);
        os.close();

        System.out.println("----------------------------------");

        System.out.println("The byte array serializable length is: " + vo.codec().length);

        testSerializablePerformance();
    }

    public static void testSerializablePerformance() throws IOException {
        ObjectVO vo = new ObjectVO();
        vo.setId(123);
        vo.setName("zhangsan");

        int loop = 1000000;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream os = null;

        long st = System.currentTimeMillis();
        for(int i =0; i < loop; i++) {
            bos = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bos);
            os.writeObject(vo);
            os.flush();
            os.close();
            byte[] b = bos.toByteArray();
            bos.close();
        }

        long en = System.currentTimeMillis();

        System.out.println("The JDK serializable cost time is : " + (en - st) + " ms");

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        st = System.currentTimeMillis();
        for(int i = 0; i < loop; i++) {
            byte[] b = vo.codec(buffer);
        }
        en = System.currentTimeMillis();

        System.out.println("The byte array serializable  cost time is : " + (en -st) + " ms");


    }
}

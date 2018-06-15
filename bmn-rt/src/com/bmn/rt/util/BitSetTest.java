package com.bmn.rt.util;

import java.util.BitSet;

/**
 * Created by Administrator on 2017/9/26.
 */
public class BitSetTest {
    public static void main(String[] args) {
        containChar("zhangsan3中");
        containCharQvp("345");

        containNum(3);
    }

    public static void containNum(int i) {
        QvpBitSet used = new QvpBitSet();
        used.set(i);
        used.set(i + 1);
        used.set(i + 2);
        used.set(63);
        System.out.println("count : " + used.count());
        used.set(64);
        System.out.println("count : " + used.count());
        used.set(127);
        System.out.println("count : " + used.count());
        used.set(128);
        System.out.println("count : " + used.count());
        used.set(511);
        used.set(1024);

        System.out.println( Integer.toBinaryString( 1 << 8));

        for(int j = 0; j < 300; j++) {
            if(used.get(j)) {
                System.out.println("haha : " + j);
            }

        }

    }

    public static void containChar(String srt) {
        BitSet used = new BitSet();
        for(int i = 0, l = srt.length(); i < l; i++) {
            used.set(srt.charAt(i));
        }



        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int size = used.size();
        System.out.println(size);

        for(int i = 0; i < size; i++) {
            if(used.get(i)) {
                sb.append((char)i);
            }
        }
        sb.append("]");
        System.out.println(sb.toString());

    }

    public static void containCharQvp(String srt) {
        QvpBitSet used = new QvpBitSet();
        for(int i = 0, l = srt.length(); i < l; i++) {
            used.set(srt.charAt(i));
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int size = used.size();
        System.out.println(size);

        for(int i = 0; i < size; i++) {
            if(used.get(i)) {
                sb.append((char)i);
            }
        }
        sb.append("]");
        System.out.println(sb.toString());

    }
}

package com.bmn.jvm;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

/**
 * 直接内存溢出
 * -verbose:gc -Xms20M -Xmx20M -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError -XX:MaxDirectMemorySize=10m
 */
public class DirectMemoryOOM {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) throws IllegalAccessException {
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);

        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        while(true) {
            System.out.println("1");
            unsafe.allocateMemory(_1MB);
        }
    }

}

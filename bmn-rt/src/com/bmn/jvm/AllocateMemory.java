package com.bmn.jvm;

/**
 * 对象内存分配
 */
public class AllocateMemory {

    private static final int _1MB = 1024 * 1024;

    public static void allocate() {
        byte[] allo1, allo2, allo3, allo4;
        allo1 = new byte[2 * _1MB];
        allo2 = new byte[2 * _1MB];
        allo3 = new byte[2 * _1MB];

        allo4 = new byte[3 * _1MB];
    }

    public static void main(String[] args) {
        allocate();
    }
}

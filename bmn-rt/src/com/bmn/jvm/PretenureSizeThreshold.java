package com.bmn.jvm;

/**
 * -XX:PretenureSizeThreshold参数
 *
 * 只在Serial, ParNew收集器有效
 *
 */
public class PretenureSizeThreshold {

    public static void allocate() {
        byte[] allo = new byte[4 * 1024 * 1024];


    }

    public static void main(String[] args) {
        allocate();
    }
}

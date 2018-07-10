package com.bmn.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * java8中，字符串常池放到了java heap
 *
 * -verbose:gc -XX:MaxMetaspaceSize=10m
 *
 * 不会导致metaspace溢出，而是java 堆内存溢出
 */
public class StringPoolOOM {

    static String base = "string";

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        for (int i=0;i< Integer.MAX_VALUE;i++){
            String str = base + base;
            base = str;
            list.add(str.intern());
        }
    }

}

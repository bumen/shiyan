package com.bmn.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 堆内存溢出
 *-verbose:gc -Xms20M -Xmx20M -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError
 */
public class HeapOOM {

    static class  OOMObject{}

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<>();
        while (true) {
            list.add(new OOMObject());
        }
    }
}

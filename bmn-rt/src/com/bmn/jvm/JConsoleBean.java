package com.bmn.jvm;

import java.util.ArrayList;
import java.util.List;

public class JConsoleBean {

    static class OOMObject{

        public byte[] placeholder = new byte[64 * 1024];
    }

    public static void filleHeap(int num) throws InterruptedException {
        List<OOMObject> list = new ArrayList<>();
        for(int i =0 ; i < num; i++) {
            Thread.sleep(500);
            System.out.println("添加一个对象 80k");
            list.add(new OOMObject());
        }

        System.gc();
    }

    public static void main(String[] args) throws InterruptedException {
        filleHeap(1000);
    }

}

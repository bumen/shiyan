package com.bmn.jvm;

/**
 * 类初始化<cinit>方法多程同步
 */
public class CInitDeadLoopClass {

    static{
        if (true) {
            System.out.println(Thread.currentThread() + "init deadclass");
            while (true) {

            }
        }
    }
}

package com.bmn.rt.util.concurrent;

/**
 * @author: zyq
 * @date: 2018/11/16
 */
public class QvpUnsafeInteger {


    private int value;

    public void increase() {
        value++;
    }

    public int getValue() {
        return this.value;
    }

}

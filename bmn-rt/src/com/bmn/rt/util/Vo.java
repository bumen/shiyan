package com.bmn.rt.util;

/**
 * Created by Administrator on 2017/9/21.
 */
public class Vo implements Cloneable{

    public int code;

    @Override
    public Vo clone() throws CloneNotSupportedException {
        return (Vo)super.clone();
    }

    @Override
    public String toString() {
        return "code : " + code;
    }
}

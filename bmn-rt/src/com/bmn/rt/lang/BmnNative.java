package com.bmn.rt.lang;

public class BmnNative {

    static {
        System.loadLibrary("");
    }

    public native int sqrt(double v);
}

package com.bmn.rt.generic.bean;

/**
 * @author: zyq
 * @date: 2018/11/29
 */
public  abstract class Convert<T> {


    public final T convert(Class<? extends T> clazz) {
        return doConvert(clazz);
    }

    public  abstract T doConvert(Class<? extends T> clazz);

}

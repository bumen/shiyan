package com.bmn.rt.generic.bean;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author: zyq
 * @date: 2018/11/29
 */
public class ConcreteConvert extends Convert<Param<?>> {


    @Override
    public Param<?> doConvert(Class<? extends Param<?>> clazz) {


        Type type = clazz;

        if(type instanceof ParameterizedType) {
            System.out.println("this is true");
        }

        return null;
    }
}

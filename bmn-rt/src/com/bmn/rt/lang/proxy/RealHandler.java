package com.bmn.rt.lang.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/5/11.
 */
public class RealHandler implements InvocationHandler {
    private Real real;

    public RealHandler(Real real) {
        this.real = real;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}

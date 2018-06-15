package com.bmn.spring.load;

import org.springframework.beans.factory.support.MethodReplacer;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/8/7.
 */
public class RelationBean implements MethodReplacer, LookupMethodSuper {
    @Override
    public Object reimplement(Object obj, Method method, Object[] args) throws Throwable {
        return null;
    }

    @Override
    public void lookup() {
        System.out.println("this is lookup");
    }
}

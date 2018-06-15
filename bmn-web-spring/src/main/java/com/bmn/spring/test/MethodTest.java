package com.bmn.spring.test;

import com.sun.beans.TypeResolver;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 2017/6/23.
 */
public class MethodTest implements IMethod{

    public static void main(String[] args) {
        MethodTest test = new MethodTest();

        Method[] methods = test.getClass().getMethods();
        for (Method m : methods) {
            test.getReturnValue(m);
            test.getParameterValue(m);
        }
    }

    public void getReturnValue(Method m) {
        Class base = m.getDeclaringClass();
        System.out.println("base: " + base);
        Type type = m.getGenericReturnType();

        Class o = TypeResolver.erase(TypeResolver.resolveInClass(base, type));
        System.out.println(o.toString());
    }

    public void getParameterValue(Method m) {
        System.out.println("begin start parameter value");

        Class base = m.getDeclaringClass();
        System.out.println("base: " + base);
        Type[] type = m.getGenericParameterTypes();

        Class[] os = TypeResolver.erase(TypeResolver.resolveInClass(base, type));
        for(Class o : os) {
            System.out.println("hahah: " + o.toString());
        }
    }

    public Integer say() {
        return 0;
    }

    @Override
    public int what() {
        return 0;
    }
}

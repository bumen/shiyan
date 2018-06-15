package com.bmn.socket.netty.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/13.
 */
public abstract class ConstantPool<T extends Constant<T>> {
    private final Map<String, T> constants = new HashMap<>();

    private int nextId = 1;

    public T valueOf(Class<?> firstNameComponent, String secondNameComponent) {
        if(firstNameComponent == null) {
            throw new NullPointerException("");
        }

        if(secondNameComponent == null) {
            throw new NullPointerException();
        }

        return valueOf(firstNameComponent.getName() + "#" + secondNameComponent);
    }


    public T valueOf(String name) {
        T c;
        synchronized (constants) {
            if(exists(name)) {
                c = constants.get(name);
            } else {
                c = newInstance0(name);
            }
        }
        return c;
    }

    public boolean exists(String name) {
        synchronized (constants) {
            return constants.containsKey(name);
        }
    }

    public T newInstance(String name) {
        if(exists(name)) {
            throw new IllegalArgumentException();
        }

        T c = newInstance0(name);
        return c;
    }

    private T newInstance0(String name) {
        synchronized (constants) {
            T c = newConstant(nextId, name);
            constants.put(name, c);
            nextId++;
            return c;
        }
    }

    protected  abstract T newConstant(int id, String name);
}

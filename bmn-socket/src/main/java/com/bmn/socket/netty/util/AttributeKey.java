package com.bmn.socket.netty.util;

/**
 * Created by Administrator on 2017/1/13.
 */
public final class AttributeKey<T> extends AbstractConstant<AttributeKey<T>> {
    private AttributeKey(int id, String name) {
        super(id, name);
    }

    private static ConstantPool<AttributeKey<Object>> pool = new ConstantPool<AttributeKey<Object>>() {
        @Override
        protected AttributeKey<Object> newConstant(int id, String name) {
            return new AttributeKey<>(id, name);
        }
    };

    public static <T> AttributeKey<T> valueOf(String name) {return (AttributeKey<T>)pool.valueOf(name);}

    public static boolean exists(String name) {
        return pool.exists(name);
    }

    public static <T> AttributeKey<T> newInstance(String name) {
        return (AttributeKey<T>) pool.newInstance(name);
    }

    public static <T> AttributeKey<T> valueOf(Class<?> firstNameComponent, String secondNameComponent) {
        return (AttributeKey<T>) pool.valueOf(firstNameComponent, secondNameComponent);
    }


}

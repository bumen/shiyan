package com.bmn.rt.lang.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/11.
 */
public class GenericTest<T, E> {

    private Map<T, ? extends Class> map;

    private E[] arrays;


    public static void main(String[] args) throws NoSuchFieldException {
        GenericTest<String, Integer> t = new GenericTest<>();
        t.testP();
    }

    public void testP() throws NoSuchFieldException {
        Field field = GenericTest.class.getDeclaredField("arrays");

        Type type = field.getGenericType();

        System.out.println(type.getTypeName());

        ParameterizedType parameterizedType = (ParameterizedType) type;

        Type[] types = parameterizedType.getActualTypeArguments();

        for(Type t : types) {
            if(t instanceof TypeVariable) {
                TypeVariable<Class> tyv = (TypeVariable)t;
                TypeVariable[] tyvs = tyv.getGenericDeclaration().getTypeParameters();
                for(TypeVariable tt : tyvs) {
                    System.out.println(tt);
                }

                System.out.println("----" + tyv.getGenericDeclaration().getName());
            }
            System.out.println(t.getClass());
        }

        Type rawType = parameterizedType.getRawType();
        System.out.println(rawType.getTypeName());

        System.out.println(type.getClass());
    }
}

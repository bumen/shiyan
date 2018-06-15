package com.bmn.rt.lang.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/12.
 */
@QvpAnnotation("AnnotationTest")
public class AnnotationTest<E> extends  Test {
    public static void main(String[] args) {
        AnnotationTest test = new AnnotationTest();
        try {
            test.test();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @QvpAnnotation("qvp")
    private int name;

    @QvpAnnotation("qvp")
    private E nameE;
    @QvpAnnotation("qvp")

    private E[] array;
    @QvpAnnotation("qvp")
    private String[] arrayS;
    @QvpAnnotation("qvp")
    private int[] arrayI;

    @QvpAnnotation("qvp")
    private List<E> list;
    @QvpAnnotation("qvp")
    private List<String> listS;

    @QvpAnnotation("qvp")
    private String str;


    public void test() throws NoSuchFieldException {

        Annotation[] annotations = AnnotationTest.class.getAnnotations();
        for (Annotation an : annotations) {
            System.out.println(an.annotationType());
        }

        AnnotatedType[] annotatedTypes = AnnotationTest.class.getAnnotatedInterfaces();

        for(AnnotatedType type : annotatedTypes) {
            System.out.println(type);
        }

        AnnotatedType annotatedType = AnnotationTest.class.getAnnotatedSuperclass();
        System.out.println(annotatedType.getType());

        Field field = AnnotationTest.class.getDeclaredField("name");
        System.out.println(field.getAnnotatedType());

         field = AnnotationTest.class.getDeclaredField("nameE");
        System.out.println(field.getAnnotatedType());

         field = AnnotationTest.class.getDeclaredField("array");
        System.out.println(field.getAnnotatedType());

         field = AnnotationTest.class.getDeclaredField("arrayI");
        System.out.println(field.getAnnotatedType());

         field = AnnotationTest.class.getDeclaredField("arrayS");
        System.out.println(field.getAnnotatedType());

        field = AnnotationTest.class.getDeclaredField("list");
        System.out.println(field.getAnnotatedType());

         field = AnnotationTest.class.getDeclaredField("listS");
        System.out.println(field.getAnnotatedType());

        field = AnnotationTest.class.getDeclaredField("str");
        System.out.println(field.getAnnotatedType());
    }
}

package com.bmn.bootstrap.util;

import java.lang.annotation.Annotation;

/**
 * @author 562655151@qq.com
 * @date 2019/5/17
 */
public abstract class AnnotationUtils {

    /**
     * 获取类上的注解
     * <span>
     * <P>如果类上没有，则递归查找实现的所有接口是否有</p>
     * <P>再查找类或接口的注解上是否有此注解</p>
     * <P>如果还没有找到，则递归查找父类是否有此注解</p>
     * </span>
     */
    public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType) {
        // 当前类
        A annotation = clazz.getAnnotation(annotationType);
        if (annotation != null) {
            return annotation;
        }
        for (Class<?> ifc : clazz.getInterfaces()) {
            annotation = findAnnotation(ifc, annotationType);
            if (annotation != null) {
                return annotation;
            }
        }
        if (!Annotation.class.isAssignableFrom(clazz)) {
            for (Annotation ann : clazz.getAnnotations()) {
                annotation = findAnnotation(ann.annotationType(), annotationType);
                if (annotation != null) {
                    return annotation;
                }
            }
        }
        Class<?> superclass = clazz.getSuperclass();
        if (superclass == null || superclass.equals(Object.class)) {
            return null;
        }
        return findAnnotation(superclass, annotationType);
    }
}

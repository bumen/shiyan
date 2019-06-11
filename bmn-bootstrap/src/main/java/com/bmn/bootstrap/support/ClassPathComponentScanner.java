package com.bmn.bootstrap.support;

import com.bmn.bootstrap.annotation.ComponentScan;
import com.bmn.bootstrap.annotation.ComponentScans;
import com.bmn.bootstrap.util.AnnotationUtils;
import java.util.LinkedHashSet;
import java.util.Set;
import org.reflections.Reflections;

/**
 * 扫描{@link ComponentScan} 指定的包
 *
 * @author 562655151@qq.com
 * @date 2019/5/21
 */
public class ClassPathComponentScanner {


    private static final ComponentScan[] EMPTY = new ComponentScan[0];


    /**
     * 获取一个指定的Reflections。如果指定多个ComponentScan。只取第1个
     */
    public Reflections scanOnePackage(Class<?> clazz) {
        ComponentScan[] scans = scanClass(clazz);
        String basePackage = scans[0].value();

        return scanBasePackage(basePackage);
    }

    /**
     * 获取所有指定的Reflections
     */
    public Set<Reflections> scan(Class<?> clazz) {
        ComponentScan[] componentScans = scanClass(clazz);
        Set<Reflections> classes = new LinkedHashSet<>();
        for (ComponentScan scan : componentScans) {
            String basePackages = scan.value();
            classes.add(scanBasePackage(basePackages));
        }

        return classes;
    }

    public ComponentScan[] scanClass(Class<?> clazz) {
        ComponentScan[] componentScans = scanClassComponentScans(clazz);
        if (componentScans == EMPTY) {
            componentScans = scanClassComponentScan(clazz);
        }

        return componentScans;
    }

    /**
     * 获取类上指定的ComponentScan
     */
    public ComponentScan[] scanClassComponentScan(Class<?> clazz) {
        ComponentScan componentScan = AnnotationUtils.findAnnotation(clazz, ComponentScan.class);
        if (componentScan == null) {
            return EMPTY;
        }
        return new ComponentScan[]{componentScan};
    }

    /**
     * 获取类上指定的ComponentScans或指定了多个ComponentScan
     */
    public ComponentScan[] scanClassComponentScans(Class<?> clazz) {
        ComponentScans componentScans = AnnotationUtils.findAnnotation(clazz, ComponentScans.class);
        if (componentScans == null) {
            return EMPTY;
        }
        return componentScans.value();
    }

    public Reflections scanBasePackage(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections;
    }
}

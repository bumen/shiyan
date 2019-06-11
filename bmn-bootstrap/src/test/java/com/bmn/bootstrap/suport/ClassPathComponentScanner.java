package com.bmn.bootstrap.suport;

import com.bmn.bootstrap.annotation.ComponentScan;
import com.bmn.bootstrap.annotation.ComponentScans;
import com.bmn.bootstrap.util.AnnotationUtils;
import com.bmn.bootstrap.util.ClassUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author zhangyuqiang02@playcrab.com
 * @date 2019/5/21
 */
public class ClassPathComponentScanner {

    static String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
    private static final ComponentScan[] EMPTY = new ComponentScan[0];

    private PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    public Set<Class<?>> scan(Set<Class<?>> classes) {
        Set<Class<?>> result = new LinkedHashSet<>();
        for (Class<?> clazz : classes) {
            result.addAll(doScan(clazz));
        }
        return result;
    }

    public Set<Class<?>> doScan(Class<?> clazz) {
        ComponentScan[] componentScans = scanClass(clazz);
        if (componentScans == EMPTY) {
            componentScans = scanClassComponentScan(clazz);
        }

        Set<Class<?>> classes = new LinkedHashSet<>();
        for (ComponentScan scan : componentScans) {
//            String[] basePackages = scan.value();
            String packages = scan.value();
            classes.addAll(scanBasePackages(new String[]{packages}));
        }

        return classes;
    }

    public ComponentScan[] scanClassComponentScan(Class<?> clazz) {
        ComponentScan componentScan = AnnotationUtils.findAnnotation(clazz, ComponentScan.class);
        if (componentScan == null) {
            return EMPTY;
        }
        return new ComponentScan[]{componentScan};
    }

    public ComponentScan[] scanClass(Class<?> clazz) {
        ComponentScans componentScans = AnnotationUtils.findAnnotation(clazz, ComponentScans.class);
        if (componentScans == null) {
            return EMPTY;
        }
        return componentScans.value();
    }

    public Set<Class<?>> scanBasePackages(String[] packages) {
        Set<Class<?>> classes = new LinkedHashSet<>();
        for (String packageStr : packages) {
            classes.addAll(scanBasePackage(packageStr));
        }

        return classes;
    }

    public Set<Class<?>> scanBasePackage(String basePackage) {
        String resolvePath = resolveBasePackage(basePackage);
        String packageSearchPath = CLASSPATH_ALL_URL_PREFIX +
            resolvePath + "/" + DEFAULT_RESOURCE_PATTERN;

        Set<Class<?>> classes = new LinkedHashSet<>();
        try {
            Path[] paths = resolver.getResources(packageSearchPath);
            for (Path path : paths) {
                try {

                    String name = ClassUtils.resolveClassName(path);
                    Class<?> clazz = Class.forName(name, false, resolver.getClassLoader());
                    classes.add(clazz);
                } catch (Throwable ex) {
                    throw new IllegalArgumentException(
                        "Cannot instantiate : " + path, ex);
                }
            }

        } catch (IOException e) {
            throw new IllegalStateException("I/O failure during classpath scanning", e);
        }

        return classes;
    }

    private String resolveBasePackage(String locationPattern) {
        return ClassUtils.convertClassNameToResourcePath(locationPattern);
    }
}

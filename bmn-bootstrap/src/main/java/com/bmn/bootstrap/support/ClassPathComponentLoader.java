package com.bmn.bootstrap.support;

import java.util.Set;
import org.reflections.Reflections;

/**
 * 包扫描加载类。目前只支持一个包。
 *
 * 主要加载{@link com.bmn.bootstrap.listener.ApplicationListener}和{@link
 * com.bmn.bootstrap.runner.ApplicationRunner}
 *
 * @author 562655151@qq.com
 * @date 2019/5/21
 */
public class ClassPathComponentLoader {

    private ClassPathComponentScanner scanner = new ClassPathComponentScanner();

    private Reflections reflections;


    public void load(Class<?> classes) {
        reflections = scanner.scanOnePackage(classes);
    }

    public <T> Set<Class<? extends T>> getSubTypesOf(final Class<T> type) {
        return reflections.getSubTypesOf(type);
    }


}

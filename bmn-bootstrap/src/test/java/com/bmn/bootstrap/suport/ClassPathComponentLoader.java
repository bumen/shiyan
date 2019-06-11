package com.bmn.bootstrap.suport;

import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;

/**
 * @author zhangyuqiang02@playcrab.com
 * @date 2019/5/21
 */
public class ClassPathComponentLoader {
    private static final ClassPathComponentLoader INSTANCE = new ClassPathComponentLoader();

    private ClassPathComponentLoader(){}

    public static ClassPathComponentLoader getInstance() {
        return INSTANCE;
    }

    private ClassPathComponentScanner scanner = new ClassPathComponentScanner();

    private Set<Reflections> cache = new HashSet<>();


    public void load(Set<Class<?>> classes) {

    }

    public <T> Set<Class<? extends T>> getSubTypesOf(final Class<T> type) {
        Set<Class<? extends T>> result = Sets.newHashSet();

        for(Reflections reflections : cache) {
            Set<Class<? extends T>> types = reflections.getSubTypesOf(type);
            if(types.isEmpty()) {
                continue;
            }

            result.addAll(types);
        }

        return result;
    }



}

package com.bmn.classloader;

/**
 * @author: zyq
 * @date: 2018/12/19
 */
public class FileClassLoader extends ClassLoader {


    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}

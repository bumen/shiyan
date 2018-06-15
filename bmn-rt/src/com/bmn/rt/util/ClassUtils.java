package com.bmn.rt.util;

/**
 * Created by Administrator on 2016/12/23.
 */
public class ClassUtils {
    public static String classPackageAsResourcePath(Class<?> clazz) {
        if(clazz == null) {
            return "";
        } else {
            String className = clazz.getName();
            int packageEndIndex = className.lastIndexOf(46);
            if(packageEndIndex == -1) {
                return "";
            } else {
                String packageName = className.substring(0, packageEndIndex);
                return packageName.replace('.', '/');
            }
        }
    }
}

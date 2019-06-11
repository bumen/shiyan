package com.bmn.bootstrap.util;

import com.bmn.bootstrap.exception.BeanInstantiationException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author 562655151@qq.com
 * @date 2019/5/20
 */
public abstract class ClassUtils {
    private static final char PACKAGE_SEPARATOR = '.';
    private static final char INNER_CLASS_SEPARATOR = '$';
    private static final char PATH_SEPARATOR = '/';
    private static final char FILE_SEPARATOR = '\\';

    public static final String CLASS_FILE_SUFFIX = ".class";

    public static final Path CLASS_ROOT_PATH;

    static {
        String name = ClassUtils.class.getPackage().getName();
        String resourceName = name.replace(".", "/");
        final URL url = ClassUtils.getDefaultClassLoader().getResource(resourceName);
        String path = url.getPath();
        int index = path.lastIndexOf(resourceName);
        if (index != -1) {
            path = path.substring(0, index);
        }

        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        CLASS_ROOT_PATH = Paths.get(path);
    }

    public static <T> T instantiateClass(Constructor<T> ctor, Object... args) {
        try {
            ReflectionUtils.makeAccessible(ctor);
            return ctor.newInstance(args);
        } catch (InstantiationException ex) {
            throw new BeanInstantiationException("Is it an abstract class?", ex);
        } catch (IllegalAccessException ex) {
            throw new BeanInstantiationException("Is the constructor accessible?", ex);
        } catch (IllegalArgumentException ex) {
            throw new BeanInstantiationException("Illegal arguments for constructor", ex);
        } catch (InvocationTargetException ex) {
            throw new BeanInstantiationException("Constructor threw exception",
                ex.getTargetException());
        }
    }

    public static String resolveClassName(Path path) {
        Path newPath = CLASS_ROOT_PATH.relativize(path);

        String packagePath = newPath.toString();

        int idx = packagePath.lastIndexOf(CLASS_FILE_SUFFIX);

        packagePath = packagePath.substring(0, idx);

        packagePath = packagePath.replace(FILE_SEPARATOR, PATH_SEPARATOR);
        packagePath = packagePath.replace(PATH_SEPARATOR, PACKAGE_SEPARATOR);

        return packagePath;
    }


    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassUtils.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

    public static String convertClassNameToResourcePath(String className) {
        return className.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);
    }
}

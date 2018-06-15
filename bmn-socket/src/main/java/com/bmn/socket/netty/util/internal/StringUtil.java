package com.bmn.socket.netty.util.internal;

import java.util.Formatter;

/**
 * Created by Administrator on 2017/1/10.
 */
public final class StringUtil {
    private static final char PACKAGE_SEPARATOR_CHAR = '.';
    public static final String NEWLINE;
    /**
     * Generates a simplified name from a {@link Class}.  Similar to {@link Class#getSimpleName()}, but it works fine
     * with anonymous classes.
     */
    public static String simpleClassName(Class<?> clazz) {
        String className = clazz.getName();
        final int lastDotIdx = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
        if (lastDotIdx > -1) {
            return className.substring(lastDotIdx + 1);
        }
        return className;
    }

    public static String simpleClassName(Object obj) {
        return simpleClassName(obj.getClass());
    }

    static {
        String newLine;

        Formatter formatter = new Formatter();
        try {
            newLine = formatter.format("%n").toString();
        } catch (Exception e) {
            // Should not reach here, but just in case.
            newLine = "\n";
        } finally {
            formatter.close();
        }

        NEWLINE = newLine;
    }
}

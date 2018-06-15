package com.bmn.spring;

import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.PropertyAccessor;

public class Test {

    private static void addStrippedPropertyPaths(List<String> strippedPaths, String nestedPath, String propertyPath) {
        int startIndex = propertyPath.indexOf("[");
        if (startIndex != -1) {
            int endIndex = propertyPath.indexOf("]");
            if (endIndex != -1) {
                String prefix = propertyPath.substring(0, startIndex);
                String key = propertyPath.substring(startIndex, endIndex + 1);
                String suffix = propertyPath.substring(endIndex + 1, propertyPath.length());
                // Strip the first key.
                strippedPaths.add(nestedPath + prefix + suffix);
                // Search for further keys to strip, with the first key stripped.
                addStrippedPropertyPaths(strippedPaths, nestedPath + prefix, suffix);
                // Search for further keys to strip, with the first key not stripped.
                addStrippedPropertyPaths(strippedPaths, nestedPath + prefix + key, suffix);
            }
        }
    }


    @org.junit.Test
    public void testPaths() {
        List<String> strippedPaths = new LinkedList<String>();
        addStrippedPropertyPaths(strippedPaths, "", "a[1][3][5][6]");

        System.out.println(strippedPaths);
    }

}

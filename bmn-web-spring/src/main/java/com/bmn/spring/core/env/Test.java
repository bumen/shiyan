package com.bmn.spring.core.env;

import org.springframework.util.SystemPropertyUtils;

/**
 * @author: zyq
 * @date: 2018/11/21
 */
public class Test {

    public static void main(String[] args) {
        testPropertyPlaceholder();
    }

    private static void testPropertyPlaceholder() {
        String placeholder = "${xxx-${java.home}}";
        placeholder = "${xxx-${java:8}}";

        String v = SystemPropertyUtils.resolvePlaceholders(placeholder, true);
        System.out.printf(v);
    }
}

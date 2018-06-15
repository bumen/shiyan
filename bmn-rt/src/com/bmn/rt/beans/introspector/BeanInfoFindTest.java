package com.bmn.rt.beans.introspector;

import java.beans.Introspector;

public class BeanInfoFindTest {


    public static void main(String[] args) {

        String[] paths = Introspector.getBeanInfoSearchPath();
        for (String p : paths) {
            System.out.println(p);
        }

        Introspector.setBeanInfoSearchPath(paths);
    }

}

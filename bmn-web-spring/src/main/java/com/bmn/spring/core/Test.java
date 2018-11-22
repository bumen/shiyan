package com.bmn.spring.core;

import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.core.SpringProperties;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Test {

    public static void main(String[] args) {
        orderTest();
    }

    private static void orderTest() {
        List<Ordered> list = new ArrayList<>();
        list.add(new OrderOne());
        list.add(new OrderTwo());
        list.add(new PriorityOrderOne());

        OrderComparator.sort(list);

        System.out.println(list);
    }



    private static final String PROPERTIES_RESOURCE_LOCATION = "spring.properties";

    private static final Properties localProperties = new Properties();

    static {
        try {
            ClassLoader cl = SpringProperties.class.getClassLoader();
            URL url = (cl != null ? cl.getResource(PROPERTIES_RESOURCE_LOCATION) :
                    ClassLoader.getSystemResource(PROPERTIES_RESOURCE_LOCATION));
            if (url != null) {
                //logger.info("Found 'spring.properties' file in local classpath");
                InputStream is = url.openStream();
                try {
                    localProperties.load(is);
                }
                finally {
                    is.close();
                }
            }
        }
        catch (IOException ex) {
//            if (logger.isInfoEnabled()) {
//                logger.info("Could not load 'spring.properties' file from local classpath: " + ex);
//            }
        }
    }
}

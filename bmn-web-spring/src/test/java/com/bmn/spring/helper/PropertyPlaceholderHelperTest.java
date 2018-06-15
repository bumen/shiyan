package com.bmn.spring.helper;

import java.util.Properties;
import org.junit.Test;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.SystemPropertyUtils;

public class PropertyPlaceholderHelperTest {


    private static PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper(
        SystemPropertyUtils.PLACEHOLDER_PREFIX, SystemPropertyUtils.PLACEHOLDER_SUFFIX, SystemPropertyUtils.VALUE_SEPARATOR, true);

    @Test
    public void parseValue() {
        String value = "${mongo.ip}:${mongo.port}";

        properties.setProperty("mongo.ip", "localhost");
        properties.setProperty("mongo.port", "8080");

        System.out.println(helper.replacePlaceholders(value, properties));
    }

    private final Properties properties = new Properties();
}

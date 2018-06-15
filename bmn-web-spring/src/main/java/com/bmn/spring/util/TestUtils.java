package com.bmn.spring.util;

import org.springframework.beans.PropertyAccessorUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/24.
 */
public class TestUtils {

    public static void main(String[] args) {
        int s = PropertyAccessorUtils.getFirstNestedPropertySeparatorIndex("map[].asdf[].a");
        System.out.println(s);

        s = PropertyAccessorUtils.getLastNestedPropertySeparatorIndex("map[].asdf[].a");
        System.out.println(s);


        PropertyTokenHolder tokens = getPropertyNameTokens("map[c.d][].a.b");

        System.out.println(tokens.canonicalName + "  " + tokens.actualName + " " + tokens.keys.toString());

    }

    static String PROPERTY_KEY_PREFIX = "[";
    static char PROPERTY_KEY_PREFIX_CHAR = '[';

    /**
     * Marker that indicates the end of a property key for an
     * indexed or mapped property like "person.addresses[0]".
     */
    static  String PROPERTY_KEY_SUFFIX = "]";
    static char PROPERTY_KEY_SUFFIX_CHAR = ']';


    private static PropertyTokenHolder getPropertyNameTokens(String propertyName) {
        PropertyTokenHolder tokens = new PropertyTokenHolder();
        String actualName = null;
        List<String> keys = new ArrayList<String>(2);
        int searchIndex = 0;
        while (searchIndex != -1) {
            int keyStart = propertyName.indexOf(PROPERTY_KEY_PREFIX, searchIndex);
            searchIndex = -1;
            if (keyStart != -1) {
                int keyEnd = propertyName.indexOf(PROPERTY_KEY_SUFFIX, keyStart + PROPERTY_KEY_PREFIX.length());
                if (keyEnd != -1) {
                    if (actualName == null) {
                        actualName = propertyName.substring(0, keyStart);
                    }
                    String key = propertyName.substring(keyStart + PROPERTY_KEY_PREFIX.length(), keyEnd);
                    if ((key.startsWith("'") && key.endsWith("'")) || (key.startsWith("\"") && key.endsWith("\""))) {
                        key = key.substring(1, key.length() - 1);
                    }
                    keys.add(key);
                    searchIndex = keyEnd + PROPERTY_KEY_SUFFIX.length();
                }
            }
        }
        tokens.actualName = (actualName != null ? actualName : propertyName);
        tokens.canonicalName = tokens.actualName;
        if (!keys.isEmpty()) {
            tokens.canonicalName +=
                    PROPERTY_KEY_PREFIX +
                            StringUtils.collectionToDelimitedString(keys, PROPERTY_KEY_SUFFIX + PROPERTY_KEY_PREFIX) +
                            PROPERTY_KEY_SUFFIX;
            tokens.keys = StringUtils.toStringArray(keys);
        }
        return tokens;
    }

    private static class PropertyTokenHolder {

        public String canonicalName;

        public String actualName;

        public String[] keys;
    }

}

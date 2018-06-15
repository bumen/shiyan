
/**
* Copyright (c) 2018 itimi.All rights reserved.
*/

package com.bmn.util;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import java.util.Map;

/**
 * 
 *
 * @date 2018-03-09
 * @author zyq
 */
public abstract class JsonUtils {

    public final static Gson gson = new Gson();

    public static <T> T fromJson(String json, Type type) {
        return gson.fromJson(json, type);
    }

    public static String toJson(Object o) {
        return gson.toJson(o);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public static Map<String,String> toMap(String data, Class<String> stringClass, Class<String> stringClass1) {
        return null;
    }
}

package com.bmn.ddd.structure.flyweight.compose;

import java.util.HashMap;
import java.util.Map;

import com.bmn.ddd.structure.flyweight.common.ConcreteFlyweight;
import com.bmn.ddd.structure.flyweight.common.Flyweight;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class FlyweightFactory {

    private static Map<String, Flyweight> cache = new HashMap<>();

    public static Flyweight factory(String innerState) {

        Flyweight flyweight = cache.get(innerState);
        if (flyweight != null) {
            return flyweight;
        }

        int len = innerState.length();
        if (len == 1) {
            flyweight = new ConcreteFlyweight(innerState);
            cache.put(innerState, flyweight);
            return flyweight;
        }

        ComposeFlyweight composeFlyweight = new ComposeFlyweight();
        for (int i = 0; i < len; i++) {
            String v = String.valueOf(innerState.charAt(i));

            composeFlyweight.add(v, factory(v));
        }

        return composeFlyweight;
    }
}

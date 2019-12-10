package com.bmn.ddd.structure.flyweight.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class FlyweightFactory {

    private static Map<String, Flyweight> cache = new HashMap<String, Flyweight>();

    public static Flyweight factory(String innerState) {
        Flyweight flyweight = cache.get(innerState);
        if (flyweight == null) {
            Flyweight fly = new ConcreteFlyweight(innerState);
            cache.put(innerState, fly);

            return fly;
        }

        return flyweight;
    }
}

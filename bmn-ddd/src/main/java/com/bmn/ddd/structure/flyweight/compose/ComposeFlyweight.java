package com.bmn.ddd.structure.flyweight.compose;

import java.util.HashMap;
import java.util.Map;

import com.bmn.ddd.structure.flyweight.common.Flyweight;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class ComposeFlyweight implements Flyweight {

    private Map<String, Flyweight> flyweightMap = new HashMap<>();


    public void add(String key, Flyweight flyweight) {
        flyweightMap.put(key, flyweight);
    }


    @Override
    public void opr(String outState) {
        for (Flyweight flyweight : flyweightMap.values()) {
            flyweight.opr(outState);
        }
    }
}

package com.bmn.ddd.structure.flyweight.compose;

import com.bmn.ddd.structure.flyweight.common.Flyweight;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Client {

    public static void main(String[] args) {
        String innerState = "abc";

        Flyweight flyweight = FlyweightFactory.factory(innerState);

        flyweight.opr("first call");

        flyweight = FlyweightFactory.factory("a");

        flyweight.opr("second call");
    }
}

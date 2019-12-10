package com.bmn.ddd.structure.flyweight.common;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Client {

    public static void main(String[] args) {

        String innerState = "a";
        Flyweight flyweight = FlyweightFactory.factory(innerState);

        String outState = "first call";

        flyweight.opr(outState);

        innerState = "b";

        flyweight = FlyweightFactory.factory(innerState);

        outState = "second call";

        flyweight.opr(outState);

        flyweight = FlyweightFactory.factory("a");

        outState = "third call";

        flyweight.opr(outState);

    }
}

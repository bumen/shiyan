package com.bmn.ddd.create.prototype;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Client {

    public static void main(String[] args) {
        ConcretePrototype prototype = new ConcretePrototype();

        ConcretePrototype clone = (ConcretePrototype) prototype.clone();

        System.out.println(prototype != clone);

        System.out.println(prototype.equals(clone));

        System.out.println(prototype.getClass() == clone.getClass());
    }
}

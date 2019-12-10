package com.bmn.ddd.create.factory.abs;

import com.bmn.ddd.create.factory.Product;

/**
 * @date 2019-12-09
 * @author zhangyuqiang02@playcrab.com
 */
public class AbsMain {

    public static void main(String[] args) {

        AbsFactory factory = new AbsFactoryA();

        test(factory);

        factory = new AbsFactoryB();

        test(factory);
    }

    private static void test(AbsFactory factory) {
        Product product = factory.createProduct();

        Fruit fruit = factory.createFruit();

        product.color();
        fruit.name();
    }

}

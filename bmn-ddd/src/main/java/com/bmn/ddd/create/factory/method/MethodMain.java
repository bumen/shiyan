package com.bmn.ddd.create.factory.method;

import com.bmn.ddd.create.factory.Product;

/**
 * @date 2019-12-09
 * @author zhangyuqiang02@playcrab.com
 */
public class MethodMain {

    public static void main(String[] args) {

        MethodFactory factory = new MethodFactoryA();

        test(factory);

        factory = new MethodFactoryB();

        test(factory);
    }

    private static void test(MethodFactory factory) {
        Product product = factory.createProduct();

        product.color();
    }
}

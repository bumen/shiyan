package com.bmn.ddd.create.factory.simple;

import com.bmn.ddd.create.factory.Product;

/**
 * @date 2019-12-09
 * @author zhangyuqiang02@playcrab.com
 */
public class SimpleMain {

    public static void main(String[] args) {

        int type = 1;

        Product product = SimpleFactory.createProduct(type);

        product.color();
    }
}

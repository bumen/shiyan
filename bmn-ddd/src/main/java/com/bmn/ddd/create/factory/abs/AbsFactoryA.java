package com.bmn.ddd.create.factory.abs;

import com.bmn.ddd.create.factory.Product;
import com.bmn.ddd.create.factory.ProductA;

/**
 * @date 2019-12-09
 * @author zhangyuqiang02@playcrab.com
 */
public class AbsFactoryA implements AbsFactory{

    public Product createProduct() {
        return new ProductA();
    }

    public Fruit createFruit() {
        return new Apple();
    }
}

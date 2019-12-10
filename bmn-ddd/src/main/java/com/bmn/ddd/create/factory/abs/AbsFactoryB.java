package com.bmn.ddd.create.factory.abs;

import com.bmn.ddd.create.factory.Product;
import com.bmn.ddd.create.factory.ProductB;

/**
 * @date 2019-12-09
 * @author zhangyuqiang02@playcrab.com
 */
public class AbsFactoryB implements AbsFactory {

    public Product createProduct() {
        return new ProductB();
    }

    public Fruit createFruit() {
        return new Banana();
    }
}

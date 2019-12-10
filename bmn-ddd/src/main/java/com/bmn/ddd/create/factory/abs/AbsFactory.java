package com.bmn.ddd.create.factory.abs;

import com.bmn.ddd.create.factory.Product;

/**
 * @date 2019-12-09
 * @author zhangyuqiang02@playcrab.com
 */
public interface AbsFactory {

    Product createProduct();

    Fruit createFruit();
}

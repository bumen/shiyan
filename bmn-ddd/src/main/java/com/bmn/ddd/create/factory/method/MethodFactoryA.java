package com.bmn.ddd.create.factory.method;

import com.bmn.ddd.create.factory.Product;
import com.bmn.ddd.create.factory.ProductA;

/**
 * @date 2019-12-09
 * @author zhangyuqiang02@playcrab.com
 */
public class MethodFactoryA implements MethodFactory {

    public Product createProduct() {
        return new ProductA();
    }
}

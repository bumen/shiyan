package com.bmn.ddd.create.factory.method;

import com.bmn.ddd.create.factory.Product;
import com.bmn.ddd.create.factory.ProductB;

/**
 * @date 2019-12-09
 * @author zhangyuqiang02@playcrab.com
 */
public class MethodFactoryB implements MethodFactory {

    public Product createProduct() {
        return new ProductB();
    }
}

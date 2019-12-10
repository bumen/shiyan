package com.bmn.ddd.create.factory.simple;

import com.bmn.ddd.create.factory.Product;
import com.bmn.ddd.create.factory.ProductA;
import com.bmn.ddd.create.factory.ProductB;

/**
 * @date 2019-12-09
 * @author zhangyuqiang02@playcrab.com
 */
public class SimpleFactory {

    public static Product createProduct(int type) {
        switch (type) {
        case 1:
            return new ProductA();
        case 2:
            return new ProductB();
        default:

        }

        return new ProductA();
    }
}

package com.bmn.ddd.create.builder.nodirector;

import com.bmn.ddd.create.builder.Product;

/**
 *
 * 省略抽象建造者+导演者
 *
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class ConcreteBuilder {
    private Product product = new Product();

    public void build1() {
        product.setName("this is product");
    }

    public void build2() {
        product.setColor(1);
    }

    public Product result() {
        return product;
    }

    public void construct() {

        build1();
        build2();

        Product product = result();
    }
}

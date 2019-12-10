package com.bmn.ddd.create.builder.noabs;

import com.bmn.ddd.create.builder.Product;

/**
 *
 * 具体建造者
 *
 * @date 2019-12-09
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
}

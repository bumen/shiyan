package com.bmn.ddd.create.builder.nobuilder;

import com.bmn.ddd.create.builder.Product;

/**
 *
 * 省略抽象建造者+导演者
 * 合并建造者角色和产品角色
 *
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class ProductA extends Product {

    private Product product = new ProductA();

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

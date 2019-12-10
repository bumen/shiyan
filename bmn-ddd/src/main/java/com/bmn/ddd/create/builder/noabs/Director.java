package com.bmn.ddd.create.builder.noabs;

import com.bmn.ddd.create.builder.Product;

/**
 *
 * 导演者
 *
 * @date 2019-12-09
 * @author zhangyuqiang02@playcrab.com
 */
public class Director {
    private ConcreteBuilder builder;

    public void construct() {

        builder = new ConcreteBuilder();
        builder.build1();
        builder.build2();

        Product product = builder.result();
    }
}

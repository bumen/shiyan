package com.bmn.ddd.create.builder.common;

import com.bmn.ddd.create.builder.Product;

/**
 *
 * 抽象建造者
 *
 * @date 2019-12-09
 * @author zhangyuqiang02@playcrab.com
 */
public interface Builder {

    void build1();

    void build2();

    Product result();
}

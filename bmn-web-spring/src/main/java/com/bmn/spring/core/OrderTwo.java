package com.bmn.spring.core;

import org.springframework.core.Ordered;

/**
 * @author: zyq
 * @date: 2018/11/21
 */
public class OrderTwo implements Ordered {

    @Override
    public int getOrder() {
        return 2;

    }

    @Override
    public String toString() {
        return "OrderTwo{}";
    }
}

package com.bmn.spring.core;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author: zyq
 * @date: 2018/11/21
 */
@Order
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

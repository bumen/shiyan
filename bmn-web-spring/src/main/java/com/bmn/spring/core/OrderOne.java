package com.bmn.spring.core;

import org.springframework.core.Ordered;

/**
 * @author: zyq
 * @date: 2018/11/21
 */
public class OrderOne implements Ordered {

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public String toString() {
        return "OrderOne{}";
    }
}

package com.bmn.spring.core;

import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

/**
 * @author: zyq
 * @date: 2018/11/21
 */
public class PriorityOrderOne implements PriorityOrdered {

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public String toString() {
        return "PriorityOrderOne{}";
    }
}

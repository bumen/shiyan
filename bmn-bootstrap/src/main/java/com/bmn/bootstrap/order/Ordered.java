package com.bmn.bootstrap.order;

/**
 * @author 562655151@qq.com
 * @date 2019/5/17
 */
public interface Ordered {
    // 最高优先级
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

    // 最低优先级
    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    int getOrder();

}

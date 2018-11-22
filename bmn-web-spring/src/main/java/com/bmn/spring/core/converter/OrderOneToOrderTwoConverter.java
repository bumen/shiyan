package com.bmn.spring.core.converter;

import com.bmn.spring.core.OrderOne;
import com.bmn.spring.core.OrderTwo;
import org.springframework.core.convert.converter.Converter;

/**
 * @author: zyq
 * @date: 2018/11/22
 */
public class OrderOneToOrderTwoConverter implements Converter<OrderOne, OrderTwo> {

    @Override
    public OrderTwo convert(OrderOne source) {
        return new OrderTwo();
    }
}

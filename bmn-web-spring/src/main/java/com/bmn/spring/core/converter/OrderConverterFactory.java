package com.bmn.spring.core.converter;

import com.bmn.spring.core.OrderOne;
import com.bmn.spring.core.OrderTwo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * @author: zyq
 * @date: 2018/11/22
 */
public class OrderConverterFactory implements ConverterFactory<OrderOne, OrderTwo>{

    @Override
    public <T extends OrderTwo> Converter<OrderOne, T> getConverter(Class<T> targetType) {
        return null;
    }
}

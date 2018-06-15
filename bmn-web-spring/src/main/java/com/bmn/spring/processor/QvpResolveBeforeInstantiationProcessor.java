package com.bmn.spring.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

import java.beans.PropertyDescriptor;

/**
 * Created by Administrator on 2017/5/10.
 */
public class QvpResolveBeforeInstantiationProcessor implements InstantiationAwareBeanPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(QvpResolveBeforeInstantiationProcessor.class);

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        logger.debug("bean: {}, beanClass: {}, 未创建， 之前。", beanName, beanClass);
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        logger.debug("bean:{}, 刚创建完成，准备注入属性。", beanName);
        return true;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        logger.debug("bean: {}, 已经创建完成， 准备注入属性: {}", beanName, pvs);
        return pvs;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        logger.debug("bean:{}, 已经创建完成， 已经注入属性， 未初始化。", beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        logger.debug("bean: {}, 已经创建完成， 已经注入属性，  已初始化。", beanName);
        return bean;
    }
}

package com.bmn.spring.bean;

import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanFactory;

/**
 * Created by Administrator on 2017/10/26.
 *
 * BeanDefinitionValueResolver
 */
public class QvpBeanDefinitionValueResolver {
    private final AbstractBeanFactory beanFactory;
    private final String beanName;
    private final BeanDefinition beanDefinition;
    private final TypeConverter typeConverter;

    public QvpBeanDefinitionValueResolver(AbstractBeanFactory beanFactory, String beanName,
                                          BeanDefinition beanDefinition,
                                          TypeConverter typeConverter) {
        this.beanFactory = beanFactory;
        this.beanName = beanName;
        this.beanDefinition = beanDefinition;
        this.typeConverter = typeConverter;
    }



    public static void main(String[] args) {
        QvpBeanDefinitionValueResolver valueResolver = new QvpBeanDefinitionValueResolver(null, null, null, null);


    }
}

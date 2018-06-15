package com.bmn.spring.xml;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Created by Administrator on 2017/10/23.
 */
public class QvpNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("annotation", new QvpAnnotationBeanDefinitionParser());
    }
}

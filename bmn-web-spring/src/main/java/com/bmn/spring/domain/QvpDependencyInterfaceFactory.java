package com.bmn.spring.domain;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;

/**
 * Created by Administrator on 2017/11/10.
 */
public class QvpDependencyInterfaceFactory implements ObjectFactory<QvpDependencyInterface> {


    @Override
    public QvpDependencyInterface getObject() throws BeansException {
        return new QvpDependencyInterface();
    }
}

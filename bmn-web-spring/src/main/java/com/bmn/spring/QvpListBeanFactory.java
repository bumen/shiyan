package com.bmn.spring;

import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

/**
 * Created by Administrator on 2017/10/26.
 */
public class QvpListBeanFactory {

    public static DefaultListableBeanFactory load(String filename) {
        String path = filename;
        ClassPathResource res = new ClassPathResource(path);
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.setAutowireCandidateResolver(new QualifierAnnotationAutowireCandidateResolver());
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(res);

        factory.getBean("");

        return factory;
    }
}

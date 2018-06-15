package com.bmn.spring;

import com.bmn.spring.load.QvpSourceExtractor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

/**
 * Created by Administrator on 2017/8/7.
 */
public class XmlBeanFactoryUtil {

    public static BeanFactory refresh(String path) {
        ClassPathResource res = new ClassPathResource(path);
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.setSourceExtractor(new QvpSourceExtractor());
        reader.loadBeanDefinitions(res);

        org.springframework.web.util.Log4jConfigListener listener;
        return factory;
    }
}

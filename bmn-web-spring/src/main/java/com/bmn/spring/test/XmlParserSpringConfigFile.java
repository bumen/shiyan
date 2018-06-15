package com.bmn.spring.test;

import com.bmn.spring.app.QvpContext;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

/**
 * Created by Administrator on 2017/5/9.
 *
 * 使用XmlBeanDefinitionReader 解析Spring配置文件
 */
public class XmlParserSpringConfigFile {

    public static void main(String[] args) {
        //1. 判断解析的spring配置文件是dtd格式还是xsd格式，
        //2. 通过判断格式，通过EntityResolver来为document配置dta文件或xsd文件(通过dtd或xsd文件来验证spring配置)
        //3. 获取document
        //4. 通过BeanDefinitionDocumentReader-->BeanDefinitionParserDelegate来解析spring配置
        //5. 获得beanDefinition
        //6. 注册到beanFactory

        String path = "spring/app.xml";
        ClassPathResource res = new ClassPathResource(path);
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(res);

        factory.getBean("qvpContext");

        factory.getBeanNamesForType(QvpContext.class);
    }

}

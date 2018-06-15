package com.bmn.spring.xml;


import org.springframework.beans.factory.parsing.EmptyReaderEventListener;
import org.springframework.beans.factory.parsing.FailFastProblemReporter;
import org.springframework.beans.factory.parsing.NullSourceExtractor;
import org.springframework.beans.factory.xml.BeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;

/**
 * Created by Administrator on 2017/10/23.
 */
public class ParseXml {
    public static void main(String[] args) throws Exception {
        Resource resource = new ClassPathResource("com/qvp/xml/qvp_xml.xml");

        LoadXml loadXml = new LoadXml(resource);
        Document document = loadXml.load();

        /**
         * 自定义，解析过程。
         */
        BeanDefinitionDocumentReader documentReader = new QvpBeanDefinitionDocumentReader();
        documentReader.registerBeanDefinitions(document, new QvpXmlReaderContext(resource));


        /**
         * spring， 解析过程
         */
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(new QvpBeanDefinitionRegistry());
        reader.setNamespaceHandlerResolver(new QvpNamespaceHandlerResolver());
        reader.setEntityResolver(new QvpEntityResolver(resource.getClass().getClassLoader()));
        reader.loadBeanDefinitions(resource);

        String[] strs = StringUtils.tokenizeToStringArray("a, b, c", ",; \t\n");
        strs = StringUtils.tokenizeToStringArray("a; b; c", ",; \t\n");
        strs = StringUtils.tokenizeToStringArray("a b c", ",; \t\n");
        strs = StringUtils.tokenizeToStringArray("a b   c", ",; \t\n");
        strs = StringUtils.tokenizeToStringArray("a\nb\nc", ",; \t\n");
        for(String s : strs) {
            System.out.println(s);
        }
    }
}

package com.bmn.spring.xml;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.parsing.*;
import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.*;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;

import java.io.IOException;

/**
 * Created by Administrator on 2017/10/23.
 */
public class QvpXmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    private NamespaceHandlerResolver namespaceHandlerResolver;

    private ProblemReporter problemReporter = new FailFastProblemReporter();

    private ReaderEventListener eventListener = new EmptyReaderEventListener();

    private SourceExtractor sourceExtractor = new NullSourceExtractor();

    private QvpXmlBeanDefinitionLoader documentLoader = new QvpXmlBeanDefinitionLoader();

    protected QvpXmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);

        documentLoader.setEntityResolver(new QvpEntityResolver(getResourceLoader().getClassLoader()));
    }

    public void setNamespaceHandlerResolver(NamespaceHandlerResolver namespaceHandlerResolver) {
        this.namespaceHandlerResolver = namespaceHandlerResolver;
    }

    @Override
    public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException {
        try {
            Document doc =  documentLoader.load(resource.getInputStream());
            return registerBeanDefinition(doc, resource);
        } catch (Exception e) {
            throw new NullPointerException();
        }
    }

    private int registerBeanDefinition(Document doc, Resource resource) {
        BeanDefinitionDocumentReader documentReader = new DefaultBeanDefinitionDocumentReader();
        documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
        return 0;
    }

    private XmlReaderContext createReaderContext(Resource resource) {
        return null;
    }
}

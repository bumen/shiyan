package com.bmn.spring.xml;

import org.springframework.beans.factory.parsing.*;
import org.springframework.beans.factory.xml.NamespaceHandlerResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.core.io.Resource;

/**
 * Created by Administrator on 2017/11/6.
 */
public class QvpXmlReaderContext extends XmlReaderContext {


    public QvpXmlReaderContext() {
        this(null, null, new EmptyReaderEventListener(), null, null, null);
    }

    public QvpXmlReaderContext(Resource resource) {
        this(resource, new FailFastProblemReporter(), new EmptyReaderEventListener(), null, new XmlBeanDefinitionReader(new QvpBeanDefinitionRegistry()), null);
    }

    public QvpXmlReaderContext(Resource resource, ProblemReporter problemReporter,
                               ReaderEventListener eventListener, SourceExtractor sourceExtractor,
                               XmlBeanDefinitionReader reader,
                               NamespaceHandlerResolver namespaceHandlerResolver) {
        super(resource, problemReporter, eventListener, sourceExtractor, reader, namespaceHandlerResolver);
    }

    @Override
    public Object extractSource(Object sourceCandidate) {
        return null;
    }
}

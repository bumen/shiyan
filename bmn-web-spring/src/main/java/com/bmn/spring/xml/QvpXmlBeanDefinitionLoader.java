package com.bmn.spring.xml;

import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.util.xml.XmlValidationModeDetector;
import org.w3c.dom.Document;
import org.xml.sax.*;

import java.io.InputStream;

/**
 * Created by Administrator on 2017/10/23.
 */
public class QvpXmlBeanDefinitionLoader {
    private EntityResolver entityResolver;

    private ErrorHandler errorHandler = new DefaultErrorHandler();

    private int validationMode = XmlValidationModeDetector.VALIDATION_XSD;

    private boolean namespaceAware = true;

    private final DocumentLoader loader = new DefaultDocumentLoader();

    public Document load(InputStream is) throws Exception {
        return this.load(new InputSource(is));
    }

    public Document load(InputSource inputSource) throws Exception {
        return loader.loadDocument(inputSource, entityResolver, errorHandler,
                validationMode, namespaceAware);
    }

    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }


    private static class DefaultErrorHandler implements ErrorHandler {

        @Override
        public void warning(SAXParseException exception) throws SAXException {

        }

        @Override
        public void error(SAXParseException exception) throws SAXException {

        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {

        }
    }
}

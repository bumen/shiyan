package com.bmn.spring.xml;

import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.DelegatingEntityResolver;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.core.io.Resource;
import org.springframework.util.xml.SimpleSaxErrorHandler;
import org.springframework.util.xml.XmlValidationModeDetector;
import org.w3c.dom.Document;
import org.xml.sax.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Administrator on 2017/10/20.
 *
 * 加载自定义的xml文件，管理自定义的xsd解析文件
 */
public class LoadXml {

    private InputSource inputSource;

    private EntityResolver entityResolver = new QvpEntityResolver(LoadXml.class.getClassLoader());

    private ErrorHandler errorHandler = new DefaultErrorHandler();

    private int validationMode = XmlValidationModeDetector.VALIDATION_XSD;

    private boolean namespaceAware = false;

    private final DocumentLoader loader = new DefaultDocumentLoader();

    public LoadXml() {
        String path = "E:/IdeaProjects/crabapple/spring/conf/spring/app.xml";
        File file = new File(path);
        try {
            inputSource = new InputSource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public LoadXml(Resource resource) {
        try {
            inputSource = new InputSource(resource.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        LoadXml loadXml = new LoadXml();
        Document document = loadXml.load();
    }

    public Document load() throws Exception {
        return loader.loadDocument(inputSource, entityResolver, errorHandler, validationMode, namespaceAware);
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

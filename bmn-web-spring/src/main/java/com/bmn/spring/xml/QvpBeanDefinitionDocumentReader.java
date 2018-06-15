package com.bmn.spring.xml;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.xml.BeanDefinitionDocumentReader;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by Administrator on 2017/11/6.
 */
public class QvpBeanDefinitionDocumentReader implements BeanDefinitionDocumentReader {

    private XmlReaderContext readerContext;

    private BeanDefinitionParserDelegate delegate;
    @Override
    public void setEnvironment(Environment environment) {

    }

    @Override
    public void registerBeanDefinitions(Document doc, XmlReaderContext readerContext) throws BeanDefinitionStoreException {
        this.readerContext = readerContext;

        Element root = doc.getDocumentElement();

        BeanDefinitionParserDelegate parent = this.delegate;
        this.delegate = createDelegate(this.readerContext, root, parent);

        parseBeanDefinitions(root, this.delegate);
    }

    protected BeanDefinitionParserDelegate createDelegate(
            XmlReaderContext readerContext, Element root, BeanDefinitionParserDelegate parentDelegate) {

        delegate = new BeanDefinitionParserDelegate(readerContext, new StandardEnvironment());
        delegate.initDefaults(root, parentDelegate);
        return delegate;
    }

    protected void parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate) {
        if (delegate.isDefaultNamespace(root)) {
            NodeList nl = root.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                if (node instanceof Element) {
                    Element ele = (Element) node;
                    if (delegate.isDefaultNamespace(ele)) {
                        parseDefaultElement(ele, delegate);
                    }
                    else {
                        delegate.parseCustomElement(ele);
                    }
                }
            }
        }
        else {
            delegate.parseCustomElement(root);
        }
    }

    private void parseDefaultElement(Element ele, BeanDefinitionParserDelegate delegate) {
       if (delegate.nodeNameEquals(ele, "bean")) {
           BeanDefinitionHolder bdHolder = delegate.parseBeanDefinitionElement(ele);

           AbstractBeanDefinition beanDefinition = null;
           try {
               beanDefinition = BeanDefinitionReaderUtils.createBeanDefinition(null, "", null);
               delegate.parsePropertyElements(ele, beanDefinition);
           } catch (ClassNotFoundException e) {
               e.printStackTrace();
           }

        }
    }
}

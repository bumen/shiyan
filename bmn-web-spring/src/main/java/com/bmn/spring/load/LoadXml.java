package com.bmn.spring.load;

import com.bmn.spring.XmlBeanFactoryUtil;
import org.springframework.beans.factory.BeanFactory;

/**
 * Created by Administrator on 2017/8/7.
 */
public class LoadXml {

    public static void main(String[] args) {
        String path = "spring/load.xml";
        BeanFactory beanFactory =  XmlBeanFactoryUtil.refresh(path);
        DefaultLoadBean bean = (DefaultLoadBean)beanFactory.getBean("defaultLoad");

        bean.beReplaced("zzz", null);
        bean.lookup();
    }
}

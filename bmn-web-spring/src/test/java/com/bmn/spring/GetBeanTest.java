package com.bmn.spring;

import com.bmn.spring.domain.DependencyInterface;
import com.bmn.spring.domain.QvpBean;
import com.bmn.spring.domain.QvpDependencyInterfaceFactory;
import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class GetBeanTest {


    @Test
    public void getBean() {

        DefaultListableBeanFactory factory = QvpListBeanFactory.load("spring/app.xml");

        factory.registerResolvableDependency(DependencyInterface.class, new QvpDependencyInterfaceFactory());

        factory.getBean("appServices");

        AppServices.getQvpContext().setBeanName("");

        factory.getBean("qvpBean");
        factory.getBean("qvpBean");
        factory.getBean("qvpBean2");
        factory.getBean("qvpBean3");
        factory.getBean("qvpBean3");

        factory.getBean(QvpBean.class);
    }
}

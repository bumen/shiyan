package com.bmn.spring.bean;


import com.bmn.spring.app.QvpContext;
import com.bmn.spring.processor.QvpDeafultBeanPostProcessor;
import com.bmn.spring.processor.QvpResolveBeforeInstantiationProcessor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * Created by Administrator on 2016/12/21.
 */
public class BeanFactoryView {

    BeanFactory f = null;

    HierarchicalBeanFactory hierarchicalBeanFactory = null;

    ListableBeanFactory listableBeanFactory = null;

    WebApplicationContext context = new XmlWebApplicationContext();

    public static void main(String[] args) {
        String path = "spring/app.xml";
        iocLoad(path);
    }

    /**
     * XmlBeanDefinitionReader 作用：读取配置文件，解析出BeanDefinition，注册到BeanFactory里。
     *
     * BeanFactory 作用： getBean
     *          1. 如果单历对象已经创建，则去缓存拿
     *          2. 如果没有创建，则创建。创建过程会处发BeanProcessor可以了解创建过程到哪一步
     *              3. 创建完成，执行初始化。
     *                      1. Aware初始化
     *                      2. InitializingBean 初始化
     *                      3. 自定义初始化方法
     *
     * @param path
     */

    //ioc 加载原理
    public static void iocLoad(String path) {
        //1. 定义资源
        ClassPathResource res = new ClassPathResource(path);
        //2. 定义工厂
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

        factory.addBeanPostProcessor(new QvpDeafultBeanPostProcessor());
        factory.addBeanPostProcessor(new QvpResolveBeforeInstantiationProcessor());
        //3. 定义加载器
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        //4. 加载
        reader.loadBeanDefinitions(res);

        QvpContext context = (QvpContext)factory.getBean("qvpContext");
        context.pre();
    }
}

package com.bmn.spring.bean;

import com.bmn.spring.QvpListBeanFactory;
import com.bmn.spring.app.QvpContext;
import com.bmn.spring.domain.DependencyInterface;
import com.bmn.spring.domain.QvpBean;
import com.bmn.spring.domain.QvpDependencyInterfaceFactory;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.ExtendedBeanInfoFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

/**
 * Created by Administrator on 2017/10/24.
 */
public class Test {

    public static void main(String[] args) throws IntrospectionException, ClassNotFoundException {
        ExtendedBeanInfoFactory beanInfoFactory = new ExtendedBeanInfoFactory();

        BeanInfo beanInfo = beanInfoFactory.getBeanInfo(QvpContext.class);

        Method[] methods = Test.class.getDeclaredMethods();
        for(Method m : methods) {
            System.out.println(m.getName() + "---" + isCandidateWriteMethod(m));
        }

        //CachedIntrospectionResults results = new CachedIntrospectionResults(QvpContext.class);

        

        BeanInfo info = Introspector.getBeanInfo(QvpContext.class);
        System.out.println(info);

        Class<?> classToFlush = QvpContext.class;
        do {
            Introspector.flushFromCaches(classToFlush);
            classToFlush = classToFlush.getSuperclass();
        }
        while (classToFlush != null && classToFlush != Object.class);


        BeanWrapper beanWrapper = new BeanWrapperImpl(QvpBeanWrapper.class);

        PropertyDescriptor[] propertyDescriptors =  beanWrapper.getPropertyDescriptors();

        for (PropertyDescriptor pd : propertyDescriptors) {
            System.out.println(pd.getPropertyType());
        }

        ConstructorArgumentValues constructorArgumentValues ;
        AutowireCandidateResolver autowireCandidateResolver;

        BeanWrapper beanWrapper2 = new BeanWrapperImpl(QvpBeanWrapper.class);
        PropertyDescriptor[] propertyDescriptors2 =  beanWrapper2.getPropertyDescriptors();
        build();

        createBeanTest();
    }

    public static void build() throws ClassNotFoundException {
        QvpBeanDefinitionBuilder builder = new QvpBeanDefinitionBuilder();
        QvpBeanDefinitionBuilder.BeanElement beanElement = builder.buildBeanElement();
        builder.initDefaults();
        AbstractBeanDefinition beanDefinition = builder.parseBeanDefinition(beanElement, "qvpContext");
    }


    public static void createBeanTest() {
        DefaultListableBeanFactory factory = QvpListBeanFactory.load("com/qvp/bean/qvp_bean.xml");
        registerBeanPostProcessors(factory);

        factory.registerResolvableDependency(DependencyInterface.class, new QvpDependencyInterfaceFactory());

        factory.getBean("qvpBean");
        factory.getBean("qvpBean");
        factory.getBean("qvpBean2");
        factory.getBean("qvpBean3");
        factory.getBean("qvpBean3");

        factory.getBean(QvpBean.class);
    }

    private static void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);
        List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<BeanPostProcessor>();
        List<BeanPostProcessor> internalPostProcessors = new ArrayList<BeanPostProcessor>();
        List<String> orderedPostProcessorNames = new ArrayList<String>();
        List<String> nonOrderedPostProcessorNames = new ArrayList<String>();
        for (String ppName : postProcessorNames) {
            if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
                BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
                priorityOrderedPostProcessors.add(pp);
                if (pp instanceof MergedBeanDefinitionPostProcessor) {
                    internalPostProcessors.add(pp);
                }
            }
            else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
                orderedPostProcessorNames.add(ppName);
            }
            else {
                nonOrderedPostProcessorNames.add(ppName);
            }
        }

        // First, register the BeanPostProcessors that implement PriorityOrdered.
        OrderComparator.sort(priorityOrderedPostProcessors);
        registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors);

        // Next, register the BeanPostProcessors that implement Ordered.
        List<BeanPostProcessor> orderedPostProcessors = new ArrayList<BeanPostProcessor>();
        for (String ppName : orderedPostProcessorNames) {
            BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
            orderedPostProcessors.add(pp);
            if (pp instanceof MergedBeanDefinitionPostProcessor) {
                internalPostProcessors.add(pp);
            }
        }
        OrderComparator.sort(orderedPostProcessors);
        registerBeanPostProcessors(beanFactory, orderedPostProcessors);

        // Now, register all regular BeanPostProcessors.
        List<BeanPostProcessor> nonOrderedPostProcessors = new ArrayList<BeanPostProcessor>();
        for (String ppName : nonOrderedPostProcessorNames) {
            BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
            nonOrderedPostProcessors.add(pp);
            if (pp instanceof MergedBeanDefinitionPostProcessor) {
                internalPostProcessors.add(pp);
            }
        }
        registerBeanPostProcessors(beanFactory, nonOrderedPostProcessors);

        // Finally, re-register all internal BeanPostProcessors.
        OrderComparator.sort(internalPostProcessors);
        registerBeanPostProcessors(beanFactory, internalPostProcessors);

    }

    private static void registerBeanPostProcessors(
            ConfigurableListableBeanFactory beanFactory, List<BeanPostProcessor> postProcessors) {

        for (BeanPostProcessor postProcessor : postProcessors) {
            beanFactory.addBeanPostProcessor(postProcessor);
        }
    }

    /**
     * 备用writeMethod
     * 1. public
     * 2. set开头
     * 3. static void 或， 非static 但是有返回值
     * 4. 一个参数 或，两个参数第1个参数必须是int类型
     * @param method
     * @return
     */
    public static boolean isCandidateWriteMethod(Method method) {
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        int nParams = parameterTypes.length;
        return (methodName.length() > 3 && methodName.startsWith("set") && Modifier.isPublic(method.getModifiers()) &&
                (!void.class.isAssignableFrom(method.getReturnType()) || Modifier.isStatic(method.getModifiers())) &&
                (nParams == 1 || (nParams == 2 && parameterTypes[0].equals(int.class))));
    }
}

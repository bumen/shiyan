package com.bmn.rt.beans;

import java.beans.AppletInitializer;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.Beans;
import java.beans.Customizer;
import java.beans.DefaultPersistenceDelegate;
import java.beans.DesignMode;
import java.beans.Encoder;
import java.beans.EventHandler;
import java.beans.EventSetDescriptor;
import java.beans.ExceptionListener;
import java.beans.Expression;
import java.beans.FeatureDescriptor;
import java.beans.IndexedPropertyChangeEvent;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PersistenceDelegate;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.beans.PropertyEditorSupport;
import java.beans.SimpleBeanInfo;
import java.beans.Statement;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeListenerProxy;
import java.beans.VetoableChangeSupport;
import java.beans.Visibility;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;

/**
 * Created by Administrator on 2017/9/14.
 */
public class Test {
    public static void main(String[] args) throws IntrospectionException {
        test1();

    }

    /*某个对象的所有属性名称都打印出来
     *
     * 1、连同父类的属性值也打印出来。（就像Students类，打印四个属性grades、name、sex、class，包括一个Object的一个class属性）
     *
     * public static BeanInfo getBeanInfo(Class<?> beanClass) throws IntrospectionException
     *
     * 2、只打印该类的属性，父类的属性不打印 （就像Students类，只打印三个属性grades、name、sex
     *
     * public static BeanInfo getBeanInfo(Class<?> beanClass,Class<?> stopClass throws IntrospectionException
     * */
    public static void test1() throws IntrospectionException {
        BeanInfo info = Introspector.getBeanInfo(Student.class, Object.class);
        PropertyDescriptor[] pds = info.getPropertyDescriptors();
        for(PropertyDescriptor pd : pds) {
            System.out.println(pd.getName());
        }

        System.out.println(info.getBeanDescriptor().getBeanClass());

        MethodDescriptor[] mds = info.getMethodDescriptors();
        for(MethodDescriptor m : mds) {
            System.out.println(m.getName());
        }

        int index = info.getDefaultPropertyIndex();
        System.out.println(index);

    }

    private  void interfaces() {
        AppletInitializer appletInitializer;
        BeanInfo beanInfo;
        Customizer customizer;
        DesignMode designMode;
        ExceptionListener exceptionListener;
        PropertyChangeListener propertyChangeListener;

        PropertyEditor propertyEditor;

        VetoableChangeListener vetoableChangeListener;
        Visibility visibility;
    }

    private void classes() {
        BeanDescriptor beanDescriptor;
        Beans beans;
        DefaultPersistenceDelegate defaultPersistenceDelegate;
        Encoder encoder;

        EventHandler eventHandler;
        EventSetDescriptor eventSetDescriptor;
        Expression expression;
        FeatureDescriptor featureDescriptor;
        IndexedPropertyChangeEvent indexedPropertyChangeEvent;
        IndexedPropertyDescriptor indexedPropertyDescriptor;
        Introspector introspector;
        MethodDescriptor methodDescriptor;
        ParameterDescriptor parameterDescriptor;

        PersistenceDelegate persistenceDelegate;
        PropertyChangeEvent propertyChangeEvent;
        PropertyChangeListenerProxy propertyChangeListenerProxy;
        PropertyChangeSupport propertyChangeSupport;
        PropertyDescriptor propertyDescriptor;
        PropertyEditorManager propertyEditorManager;
        PropertyEditorSupport propertyEditorSupport;
        SimpleBeanInfo simpleBeanInfo;
        Statement statement;
        VetoableChangeListenerProxy vetoableChangeListenerProxy;
        VetoableChangeSupport vetoableChangeSupport;
        XMLDecoder xmlDecoder;
        XMLEncoder xmlEncoder;












    }
}

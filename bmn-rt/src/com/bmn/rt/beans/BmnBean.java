package com.bmn.rt.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;

public class BmnBean {

    public void Introspector () throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(Student.class);
    }
}

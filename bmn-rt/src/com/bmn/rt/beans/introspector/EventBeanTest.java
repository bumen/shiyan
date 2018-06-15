package com.bmn.rt.beans.introspector;

import com.bmn.bean.EventListenerBean;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;

public class EventBeanTest {

    public static void main(String[] args) throws IntrospectionException {
        BeanInfo info = Introspector.getBeanInfo(EventListenerBean.class);
    }

}

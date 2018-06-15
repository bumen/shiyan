package com.bmn.rt.beans.introspector;

import com.bmn.bean.BranchBean;
import com.bmn.bean.LeafBean;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

public class BeanFlagTest {


    public static void use_all_beaninfo() throws IntrospectionException {
        BeanInfo info = Introspector.getBeanInfo(BranchBean.class, Introspector.USE_ALL_BEANINFO);
        for(PropertyDescriptor pd : info.getPropertyDescriptors()) {
            System.out.println(pd.getName());
        }
    }

    public static void ignore_immediate_beaninfo() throws IntrospectionException {
        BeanInfo info = Introspector.getBeanInfo(BranchBean.class, Introspector.IGNORE_IMMEDIATE_BEANINFO);
        for(PropertyDescriptor pd : info.getPropertyDescriptors()) {
            System.out.println(pd.getName());
        }
    }

    public static void ignore_all_beaninfo() throws IntrospectionException {
        BeanInfo info = Introspector.getBeanInfo(BranchBean.class, Introspector.IGNORE_ALL_BEANINFO);
        for(PropertyDescriptor pd : info.getPropertyDescriptors()) {
            System.out.println(pd.getName());
        }
    }

    public static void main(String[] args)  {
        try {
            use_all_beaninfo();

            System.out.println("========");

            ignore_immediate_beaninfo();

            System.out.println("========");

            ignore_all_beaninfo();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
    }

}

package com.bmn.spring.context;

import com.bmn.spring.context.annbean.AnnBeanConfig;
import com.bmn.spring.context.annbean.Course;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author: zyq
 * @date: 2018/11/27
 */
public class Test {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AnnBeanConfig.class);
        context.refresh();

        Course c = (Course)context.getBean(Course.class);

        c.getModule().getAssignment().say();

    }
}

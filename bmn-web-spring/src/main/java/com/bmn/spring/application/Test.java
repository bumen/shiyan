package com.bmn.spring.application;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * Created by Administrator on 2017/11/10.
 */
public class Test {
    public static void main(String[] args) {

        FileSystemXmlApplicationContext applicationContext;

        ClassPathXmlApplicationContext classPathXmlApplicationContext;

        AnnotationConfigApplicationContext annotationConfigApplicationContext;

        XmlWebApplicationContext xmlWebApplicationContext;

        AnnotationConfigWebApplicationContext annotationConfigWebApplicationContext;

        GenericXmlApplicationContext genericXmlApplicationContext;
    }
}

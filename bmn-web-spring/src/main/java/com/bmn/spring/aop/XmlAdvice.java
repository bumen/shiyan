package com.bmn.spring.aop;

import org.aspectj.lang.JoinPoint;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

/**
 * Created by Administrator on 2017/5/12.
 */
public class XmlAdvice implements MethodBeforeAdvice{

    private void doBefore(JoinPoint joinPoint) {
        System.out.println("asdfasdf");
    }
}

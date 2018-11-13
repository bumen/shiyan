package com.bmn.spring.aop;

import org.aspectj.lang.JoinPoint;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/5/12.
 */
public class XmlAdvice implements MethodBeforeAdvice{

    private void doBefore(JoinPoint joinPoint) {
        System.out.println("asdfasdf");
    }

    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {

    }
}

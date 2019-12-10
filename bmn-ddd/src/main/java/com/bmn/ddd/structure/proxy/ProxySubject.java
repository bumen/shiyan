package com.bmn.ddd.structure.proxy;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class ProxySubject implements Subject {

    private RealSubject realSubject;


    public void preInvoke() {
        System.out.println("this is pre invoke");
    }

    @Override
    public void invoke() {
        preInvoke();
        if (realSubject == null) {
            realSubject = new RealSubject();
        }
        realSubject.invoke();
        postInvoke();
    }

    public void postInvoke() {
        System.out.println("this is post invoke");
    }
}

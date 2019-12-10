package com.bmn.ddd.structure.proxy;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class RealSubject implements Subject {

    @Override
    public void invoke() {
        System.out.println("this is real subject");
    }
}

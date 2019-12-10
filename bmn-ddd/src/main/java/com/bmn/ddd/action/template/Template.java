package com.bmn.ddd.action.template;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public abstract class Template {

    public int calc() {
        int a = getA();
        int b = getB();

        return a + b;
    }

    abstract int getA();

    abstract int getB();
}

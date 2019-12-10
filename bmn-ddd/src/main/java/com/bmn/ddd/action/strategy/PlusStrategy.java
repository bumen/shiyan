package com.bmn.ddd.action.strategy;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class PlusStrategy implements Strategy {

    @Override
    public void calc(int a, int b) {
        System.out.println("a + b = " + (a + b));
    }
}

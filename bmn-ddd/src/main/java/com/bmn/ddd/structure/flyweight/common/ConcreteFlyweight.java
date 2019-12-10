package com.bmn.ddd.structure.flyweight.common;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class ConcreteFlyweight implements Flyweight {

    private String innerState;

    public ConcreteFlyweight(String innerState) {
        this.innerState = innerState;
    }

    @Override
    public void opr(String outerState) {
        System.out.println("this "+ this.innerState +" is outerState: " + outerState);
    }
}

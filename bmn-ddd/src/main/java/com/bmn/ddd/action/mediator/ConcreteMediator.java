package com.bmn.ddd.action.mediator;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class ConcreteMediator implements Mediator {

    private ColleagueA colleagueA;
    private ColleagueB colleagueB;

    public void create() {
        colleagueA = new ColleagueA(this);
        colleagueB = new ColleagueB(this);
    }

    @Override
    public void colleagueChanged(Colleague colleague) {
        colleagueA.action();
        colleagueB.action();
    }
}

package com.bmn.ddd.action.mediator;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Client {

    public static void main(String[] args) {
        ConcreteMediator mediator = new ConcreteMediator();

        mediator.create();

        Colleague colleague = new ColleagueA(mediator);

        mediator.colleagueChanged(colleague);
    }
}

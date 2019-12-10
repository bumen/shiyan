package com.bmn.ddd.action.mediator;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class ColleagueA extends Colleague {

    public ColleagueA(Mediator mediator) {
        super(mediator);
    }

    @Override
    public void action() {
        System.out.println("this is colleague a");
    }
}

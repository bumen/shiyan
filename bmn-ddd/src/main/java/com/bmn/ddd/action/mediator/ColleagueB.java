package com.bmn.ddd.action.mediator;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class ColleagueB extends Colleague {

    public ColleagueB(Mediator mediator) {
        super(mediator);
    }

    @Override
    public void action() {
        System.out.println("this is colleague b");
    }
}

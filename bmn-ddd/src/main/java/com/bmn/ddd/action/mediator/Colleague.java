package com.bmn.ddd.action.mediator;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public abstract class Colleague {

    private Mediator mediator;


    public Colleague(Mediator mediator) {
        this.mediator = mediator;
    }

    public abstract void action();


    void change() {
        this.mediator.colleagueChanged(this);
    }


}

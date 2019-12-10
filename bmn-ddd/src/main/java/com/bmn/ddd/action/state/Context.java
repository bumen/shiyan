package com.bmn.ddd.action.state;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Context {

    private State state;


    public void setState(State state) {
        this.state = state;
    }

    public void action() {
        this.state.action(this);
    }
}

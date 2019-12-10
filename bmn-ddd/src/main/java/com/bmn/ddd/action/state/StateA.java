package com.bmn.ddd.action.state;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class StateA implements State {

    @Override
    public void action(Context context) {
        System.out.println("this is state a");

        context.setState(new StateB());
    }
}

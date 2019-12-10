package com.bmn.ddd.action.state;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Client {

    public static void main(String[] args) {
        State state = new StateA();

        Context context = new Context();

        context.setState(state);

        context.action();

        context.action();
    }

}

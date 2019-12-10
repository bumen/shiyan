package com.bmn.ddd.action.command.common;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Invoker {

    private Command command;

    public void receive(Command command) {
        this.command = command;
    }

    public void action() {
        this.command.execute();
    }
}

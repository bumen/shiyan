package com.bmn.ddd.action.command.common;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class CommandA extends Command {
    private Receiver receiver;

    public CommandA(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    void execute() {
        this.receiver.action2();
    }
}

package com.bmn.ddd.action.command.common;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class CommandB extends Command {
    private Receiver receiver;

    public CommandB(Receiver receiver) {
        this.receiver = receiver;
    }


    @Override
    void execute() {
        receiver.action();
    }
}

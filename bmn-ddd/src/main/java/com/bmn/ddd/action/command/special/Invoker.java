package com.bmn.ddd.action.command.special;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Invoker {

    public void receive(Command command) {
        Receiver receiver = Receiver.getReceiver(command.getType());
        receiver.action();
    }
}

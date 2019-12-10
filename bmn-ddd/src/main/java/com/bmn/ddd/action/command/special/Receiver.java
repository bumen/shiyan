package com.bmn.ddd.action.command.special;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public abstract class Receiver {

    public static Receiver getReceiver(int type) {
        if (type == 1) {
            return new ReceiverA();
        }

        return new ReceiverB();
    }

    abstract void action();
}

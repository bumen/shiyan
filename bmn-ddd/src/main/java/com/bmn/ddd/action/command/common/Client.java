package com.bmn.ddd.action.command.common;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Client {

    public static void main(String[] args) {
        Receiver receiver = new Receiver();

        Command command = new CommandB(receiver);

        Invoker invoker = new Invoker();

        invoker.receive(command);

        Thread t = new Thread(() -> {

            invoker.action();
        });

        t.start();
    }
}

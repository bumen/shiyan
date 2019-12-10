package com.bmn.ddd.action.command.special;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Client {

    public static void main(String[] args) {
        Invoker invoker = new Invoker();

        Command command = new CommandA();
        invoker.receive(command);

        command = new CommandB();

        invoker.receive(command);
    }
}

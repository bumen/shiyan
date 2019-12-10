package com.bmn.ddd.action.chain;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Client {

    public static void main(String[] args) {
        Handler handler = new HandlerA();
        Handler handler1 = new HandlerB();

        handler.setHandler(handler1);

        handler.handle();
    }
}

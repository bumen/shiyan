package com.bmn.ddd.action.chain;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class HandlerA extends Handler {

    @Override
    void handle() {
        if (handler != null) {
            handler.handle();
            return;
        }

        System.out.println("this is handler a handle");
    }
}

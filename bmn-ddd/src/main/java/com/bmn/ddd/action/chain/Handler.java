package com.bmn.ddd.action.chain;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public abstract class Handler {

    protected Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    abstract void handle();
}

package com.bmn.ddd.action.observer;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public interface Observer {

    void update(Subject subject, Object args);
}

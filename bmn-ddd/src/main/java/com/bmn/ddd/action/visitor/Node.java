package com.bmn.ddd.action.visitor;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public interface Node {

    public void accept(Visitor visitor);

    void opr();
}

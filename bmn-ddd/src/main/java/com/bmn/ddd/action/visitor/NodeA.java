package com.bmn.ddd.action.visitor;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class NodeA implements Node {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void opr() {
        System.out.println("this is node a");
    }
}

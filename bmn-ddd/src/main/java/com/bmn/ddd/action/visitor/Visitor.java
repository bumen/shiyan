package com.bmn.ddd.action.visitor;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public interface Visitor {

    public void visit(NodeA nodeA);

    public void visit(NodeB nodeB);
}

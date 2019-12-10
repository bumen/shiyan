package com.bmn.ddd.action.visitor;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class VisitorB implements Visitor {

    @Override
    public void visit(NodeA nodeA) {
        nodeA.opr();
    }

    @Override
    public void visit(NodeB nodeB) {
        nodeB.opr();
    }
}

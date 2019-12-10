package com.bmn.ddd.action.visitor;

/**
 *
 * 新加node时，需要改支visitor添加一个新的访问方法，代表可以访问新加的结点
 *
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class VisitorA implements Visitor {

    @Override
    public void visit(NodeA nodeA) {
        System.out.println("this is a and a");
        nodeA.opr();
    }

    @Override
    public void visit(NodeB nodeB) {
        System.out.println("this is a and b");
        nodeB.opr();
    }
}

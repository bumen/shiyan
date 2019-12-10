package com.bmn.ddd.action.visitor;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Client {

    public static void main(String[] args) {
        Visitor visitor = new VisitorA();

        ObjectStructure structure = new ObjectStructure();

        structure.add(new NodeA());
        structure.add(new NodeB());

        structure.visit(visitor);

    }
}

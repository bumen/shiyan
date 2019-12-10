package com.bmn.ddd.action.visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class ObjectStructure {

    private List<Node> nodes = new ArrayList<>();


    public void add(Node node) {
        this.nodes.add(node);
    }

    public void visit(Visitor visitor) {

        for (Node node : nodes) {
            node.accept(visitor);
        }
    }

}

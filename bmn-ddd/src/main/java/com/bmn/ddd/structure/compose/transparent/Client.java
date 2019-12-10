package com.bmn.ddd.structure.compose.transparent;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Client {

    public static void main(String[] args) {
        Component component = new Leaf();

        Component component1 = new Composite();

        component1.add(component);

    }

}

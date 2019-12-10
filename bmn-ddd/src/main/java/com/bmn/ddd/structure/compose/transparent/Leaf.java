package com.bmn.ddd.structure.compose.transparent;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Leaf implements Component {

    public void add(Component component) {

    }

    public void remove(Component component) {

    }

    public void operation() {
        System.out.println("this is leaf");
    }
}

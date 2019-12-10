package com.bmn.ddd.structure.compose.safity;

import java.util.LinkedList;
import java.util.List;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Composite implements Component {

    private List<Component> components = new LinkedList<Component>();

    public void add(Component component) {

        this.components.add(component);
    }

    public void remove(Component component) {
        this.components.remove(component);
    }

    public void opr() {
        for (Component component : components) {
            component.opr();
        }
    }
}

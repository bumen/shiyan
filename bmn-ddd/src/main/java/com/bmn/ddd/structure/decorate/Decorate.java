package com.bmn.ddd.structure.decorate;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public abstract class Decorate implements Component {

    protected Component component;

    public Decorate(Component component) {
        this.component = component;
    }

}

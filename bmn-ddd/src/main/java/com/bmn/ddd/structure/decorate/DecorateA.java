package com.bmn.ddd.structure.decorate;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class DecorateA extends Decorate {

    public DecorateA(Component component) {
        super(component);
    }

    @Override
    public void operation() {
        System.out.println("this is decorate a before");
        this.component.operation();
        System.out.println("this is decorate a after");
    }
}

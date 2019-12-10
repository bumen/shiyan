package com.bmn.ddd.structure.decorate;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class ConcreteComponent implements Component {

    @Override
    public void operation() {
        System.out.println("this is concrete component");
    }
}

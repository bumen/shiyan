package com.bmn.ddd.structure.adapter.obj;

import com.bmn.ddd.structure.adapter.Adaptee;
import com.bmn.ddd.structure.adapter.Target;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Adapter implements Target {

    private Adaptee adaptee;

    public Adapter(Adaptee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void oldMethod() {
        this.adaptee.oldMethod();
    }

    @Override
    public void newMethod() {
        System.out.println("this is new");
    }
}

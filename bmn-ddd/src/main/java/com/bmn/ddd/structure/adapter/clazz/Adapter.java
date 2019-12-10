package com.bmn.ddd.structure.adapter.clazz;

import com.bmn.ddd.structure.adapter.Adaptee;
import com.bmn.ddd.structure.adapter.Target;

/**
 *
 * 类结构适配器
 *
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Adapter extends Adaptee implements Target {

    public void newMethod() {
        System.out.println("this is new");
    }
}

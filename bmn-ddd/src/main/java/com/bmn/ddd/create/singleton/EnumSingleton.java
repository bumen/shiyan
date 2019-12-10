package com.bmn.ddd.create.singleton;

/**
 * @date 2019-12-09
 * @author zhangyuqiang02@playcrab.com
 */
public enum  EnumSingleton {

    INSTANCE;

    public void naem() {
        System.out.println("this is enum singleton");
    }
}

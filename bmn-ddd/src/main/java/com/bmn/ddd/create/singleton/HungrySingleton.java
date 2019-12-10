package com.bmn.ddd.create.singleton;

/**
 * @date 2019-12-09
 * @author zhangyuqiang02@playcrab.com
 */
public class HungrySingleton {

    private HungrySingleton() {

    }

    private static final HungrySingleton SINGLETON = new HungrySingleton();

    public static HungrySingleton getInstance() {
        return SINGLETON;
    }
}

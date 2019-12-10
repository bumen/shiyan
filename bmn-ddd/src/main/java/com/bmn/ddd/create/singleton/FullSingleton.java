package com.bmn.ddd.create.singleton;

/**
 * @date 2019-12-09
 * @author zhangyuqiang02@playcrab.com
 */
public class FullSingleton {
    private static FullSingleton SINGLETON;

    private FullSingleton() {

    }

    public static FullSingleton getInstance() {
        if (SINGLETON == null) {
            SINGLETON = new FullSingleton();
        }

        return SINGLETON;
    }
}

package com.bmn.ddd.create.singleton;

/**
 * @date 2019-12-09
 * @author zhangyuqiang02@playcrab.com
 */
public class StaticSingleton {

    private StaticSingleton() {

    }


    private static class StaticSingletonHolder {

        private static final StaticSingleton INSTANCE = new StaticSingleton();
    }

    public static StaticSingleton getInstance() {
        return StaticSingletonHolder.INSTANCE;
    }
}

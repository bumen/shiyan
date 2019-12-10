package com.bmn.ddd.create.singleton;

/**
 * @date 2019-12-09
 * @author zhangyuqiang02@playcrab.com
 */
public class DCLFullSingleton {
    private static volatile DCLFullSingleton SINGLETON;

    private DCLFullSingleton() {}

    private static DCLFullSingleton getInstance() {

        if (SINGLETON == null) {
            synchronized (DCLFullSingleton.class) {
                if (SINGLETON == null) {
                    SINGLETON = new DCLFullSingleton();
                }
            }
        }

        return SINGLETON;
    }
}

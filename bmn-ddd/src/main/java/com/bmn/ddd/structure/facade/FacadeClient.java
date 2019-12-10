package com.bmn.ddd.structure.facade;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class FacadeClient {

    private Light light = new Light();
    private Television tv = new Television();

    public void active() {
        light.turnOn();
        tv.turnOn();
    }

    public void deactivate() {
        tv.turnOff();
        light.turnOff();
    }

}

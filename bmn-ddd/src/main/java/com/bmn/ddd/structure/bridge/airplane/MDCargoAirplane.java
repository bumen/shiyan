package com.bmn.ddd.structure.bridge.airplane;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class MDCargoAirplane extends MD implements AirplanePeer{

    @Override
    public void fly() {
        System.out.println("this is md cargo airplane");
    }
}

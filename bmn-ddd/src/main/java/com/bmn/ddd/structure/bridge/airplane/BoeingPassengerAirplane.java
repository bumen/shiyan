package com.bmn.ddd.structure.bridge.airplane;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class BoeingPassengerAirplane extends Boeing implements AirplanePeer {

    @Override
    public void fly() {
        System.out.println("this is boeing passenger airplane");
    }
}

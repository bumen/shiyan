package com.bmn.ddd.structure.bridge.airplane;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class AirbusPassengerAirplane extends Airbus implements AirplanePeer {

    @Override
    public void fly() {
        System.out.println("this is airbus passenger airplane");
    }
}
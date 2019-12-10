package com.bmn.ddd.structure.bridge.airplane;

/**
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class PassengerAirplane extends Airplane {

    public PassengerAirplane() {
        market = new MDPassengerAirplane();
    }

    @Override
    void fly() {
        AirplanePeer peer  = (AirplanePeer) market;
        if (peer != null) {
            peer.fly();
        }
    }
}

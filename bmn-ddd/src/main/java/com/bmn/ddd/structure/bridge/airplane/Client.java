package com.bmn.ddd.structure.bridge.airplane;

/**
 *
 * 1. 两个体系结构airplane + marker
 *
 * 2. 客户端使用airplane 间接操作marker的具体实现去执行
 *
 * 3. 关系绑定参考
 *  1）Component中绑定ComponentPeer, 应该是使用访问者模式
 *  2）jdbc比较特殊，所有类都是通过Connection类创建出来，所以只要实现connection就可以
 *
 * @date 2019-12-10
 * @author zhangyuqiang02@playcrab.com
 */
public class Client {

    public static void main(String[] args) {


        Airplane airplane = new PassengerAirplane();

        airplane.fly();
    }
}

package com.bmn.socket.zmq;

/**
 * Created by Administrator on 2017/6/13.
 */
public class QvpSignalerTest {

    public static void main(String[] args) {
        QvpSignaler signaler = new QvpSignaler();


        signaler.send();
        signaler.send();

        System.out.println("select now...");

        boolean hadMsg = signaler.waitEvent(-1);

        signaler.recv();
        signaler.recv();
        signaler.recv();

        System.out.println("had Msg :" + hadMsg);
    }
}

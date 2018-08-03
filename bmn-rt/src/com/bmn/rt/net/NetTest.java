package com.bmn.rt.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetTest {

    public static void main(String[] args) throws UnknownHostException {
        //InetAddress inetAddress = InetAddress.getByName("game.itimi.cn");

        //System.out.println(inetAddress);

        InetAddress[] adds = InetAddress.getAllByName("www.microsoft.com");
        for (InetAddress ad : adds) {
            System.out.println(ad);
        }

        System.out.println(InetAddress.getLoopbackAddress());

        System.out.println(InetAddress.getLocalHost());

        byte[] b = new byte[]{(byte)192,(byte)168,1,83};
        InetAddress inetAddress = InetAddress.getByAddress(b);
        System.out.println(inetAddress.getHostAddress());

        InetAddress local = InetAddress.getLoopbackAddress();
        System.out.println(local.isAnyLocalAddress());
        System.out.println(local.isLinkLocalAddress());
        System.out.println(local.isLoopbackAddress());

    }

}

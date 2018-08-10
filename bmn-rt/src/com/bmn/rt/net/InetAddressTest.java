package com.bmn.rt.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressTest {

    public static void main(String[] args) throws UnknownHostException {
        String[] ips = new String[] {"0.0.0.0", "127.0.0.1", "192.168.254.32","FE80:0:0:0:0:0:0:0", "www.oreilly.com", "224.0.2.1", "FF01:0:0:0:0:0:0:1", "FF05:0:0:0:0:0:0:101", "0::1", "sap.mcast.net","all-routers.mcast.net"};

        for (String ip : ips) {
            InetAddress address = InetAddress.getByName(ip);
            if (address.isAnyLocalAddress()) {
                System.out.println(address + " is a wildcard address.");
            }

            if (address.isLoopbackAddress()) {
                System.out.println(address + " is lookback address.");
            }

            if (address.isLinkLocalAddress()) {
                System.out.println(address + " is a link-local address.");
            } else if (address.isSiteLocalAddress()) {
                System.out.println(address + " is a site-local address.");
            } else {
                System.out.println(address + " is a global address.");
            }

            if (address.isMulticastAddress()) {
                if (address.isMCGlobal()) {
                    System.out.println(address + " is a global multicast address.");
                } else if (address.isMCOrgLocal()) {
                    System.out.println(address + " is a organization wide multicast address.");
                } else if (address.isMCSiteLocal()) {
                    System.out.println(address + " is a site wide multicast address.");
                } else if (address.isMCLinkLocal()) {
                    System.out.println(address + " is a subnet wide multicast address.");
                } else if (address.isMCNodeLocal()) {
                    System.out.println(address + " is an interface-local multicast address.");
                } else {
                    System.out.println(address + " is an unknown multicast address type.");
                }
            } else {
                System.out.println(address + " is a unicast address.");
            }

            System.out.println();
        }


    }
}

package com.bmn.rt.net;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class NetworkInterfaceTest {

    public static void main(String[] args) throws SocketException, UnknownHostException {

        NetworkInterface networkInterface = NetworkInterface.getByName("eth0");
        System.out.println(networkInterface);

        InetAddress inetAddress = InetAddress.getByName("127.0.0.1");

        NetworkInterface networkInterface1 = NetworkInterface.getByInetAddress(inetAddress);
        System.out.println(networkInterface1);

        Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements()) {
            System.out.println(e.nextElement());
        }
    }

}

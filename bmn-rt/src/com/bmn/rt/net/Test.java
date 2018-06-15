package com.bmn.rt.net;

import java.net.*;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Administrator on 2017/9/18.
 */
public class Test {
    public static void main(String[] args) throws UnknownHostException, SocketException, URISyntaxException {
        IDNTest();

        InetAddress address = InetAddress.getLocalHost();
        System.out.println(address);
        address = InetAddress.getLoopbackAddress();
        System.out.println(address);

        address = InetAddress.getByName("www.baidu.com");
        System.out.println(address);

        InetAddress[] ads =  InetAddress.getAllByName("www.baidu.com");
        for(InetAddress id : ads) {
            System.out.println("all address " + id);
        }

        NetworkInterface net = NetworkInterface.getByIndex(5);
        System.out.println(net);

        Enumeration<NetworkInterface> nets =  NetworkInterface.getNetworkInterfaces();
        while(nets.hasMoreElements()) {
            NetworkInterface n = nets.nextElement();
            List<InterfaceAddress> list = n.getInterfaceAddresses();
            for(InterfaceAddress d : list) {
                System.out.println(d);
            }

            Enumeration<InetAddress> ds =  n.getInetAddresses();
           while(ds.hasMoreElements()) {
               System.out.println("asdfasf " + ds.nextElement());
           }


        }

        uri();
    }

    public static void uri() throws URISyntaxException {
        System.out.println("uri test....................");
        URI uriBase = new URI("http://www.somedomain.com");
        System.out.println(uriBase.getHost());
        URI uriRelative = new URI("x/../y");
        System.out.println(uriRelative.normalize());

        URI uriResolve = uriBase.resolve(uriRelative); // http://www.somedomain.com/y
        System.out.println(uriResolve);

        URI uriRelativized = uriBase.relativize(uriRelative); // y
        System.out.println(uriRelativized);
    }

    public static void IDNTest() {
        String input = "www.your22Name.com";
        String ascii = IDN.toASCII(input);
        String unicode = IDN.toUnicode(input);

        System.out.println("Input:"+ input);
        System.out.println("toAscii (input):"+ ascii);
        System.out.println("toUnicode (input):"+ unicode);

    }

    private void interfaces() {
        ContentHandlerFactory contentHandlerFactory;
        CookiePolicy cookiePolicy;
        DatagramSocketImplFactory datagramSocketImplFactory;
        FileNameMap fileNameMap;
        ProtocolFamily protocolFamily;
        SocketImplFactory socketImplFactory;
        SocketOption socketOption;
        SocketOptions socketOptions;
        URLStreamHandlerFactory urlStreamHandlerFactory;

    }

    private void classes() {
        Authenticator authenticator;
        PasswordAuthentication passwordAuthentication;

        CacheRequest cacheRequest;
        CacheResponse cacheResponse;

        CookieHandler cookieHandler;
        CookieManager cookieManager;
        HttpCookie httpCookie;

        HttpURLConnection httpURLConnection;
        JarURLConnection jarURLConnection;

        //udp
        DatagramPacket datagramPacket;
        DatagramSocket datagramSocket;
        MulticastSocket multicastSocket;
        DatagramSocketImpl datagramSocket1;


        IDN idn;

        //ip
        InetAddress inetAddress;
        Inet4Address inet4Address;
        Inet6Address inet6Address;

        InterfaceAddress interfaceAddress;
        NetworkInterface networkInterface;





        Proxy proxy;
        ProxySelector proxySelector;

        ResponseCache responseCache;
        SecureCacheResponse secureCacheResponse;

        //tcp
        ServerSocket serverSocket;
        Socket socket;
        SocketAddress socketAddress;
        InetSocketAddress inetSocketAddress;
        SocketImpl socket1;
        StandardSocketOptions standardSocketOptions;

        URI uri;
        URL url;
        URLStreamHandler urlStreamHandler;

        URLClassLoader urlClassLoader;

        URLConnection urlConnection;
        ContentHandler contentHandler;

        URLDecoder urlDecoder;
        URLEncoder urlEncoder;


    }
}

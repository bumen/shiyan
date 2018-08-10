package com.bmn.rt.net;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlTest {

    public static void main(String[] args) {
        protocal();
    }

    public static void protocal() {
        try {
            URL u = new URL("http", "www.eff.org", "/b.html#intro");
            System.out.println(u);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String[] pArray = {"http://www.baidu.org",
            "https://www.amazon.com/exec/obidos/order2/",
            "ftp://metalab.unc.edu/pub/languages/java/javafaq/",
            "mailto:elharo@metalab.unc.edu",
            "telnet://dibner.poly.edu/",
            "file://etc/passwd",
            "gopher://gopher.anc.org.za/",
            "ldap://ldap.itd.umich.edu/o=adsfasdf,c=us?postal",
            "jar:http://cafeaul.org/books/javaio/ioexamples/javaio.jar!/com/qvp/io/s.class",
            "nfs://uto.poly.edu/usr/tmp/",
            "jdbc:mysql://luna.com:3306/news",
            "rmi://metalb.com/radmysql",
            "doc:/UserGuide/release.html",
            "netdoc:/UserGuide/release.html",
            "systemresource://www.adc.org/+/index.html",
            "verbatim:http://www.baidu.com/*"

        };

        for (String url : pArray) {
            try {
                URL u = new URL(url);
                System.out.println(u.getProtocol() + " is supported");
            } catch (MalformedURLException e) {
                // e.printStackTrace();
                String p = url.substring(0, url.indexOf(":"));
                System.out.println(p + " is not supported");
            }
        }
    }

}

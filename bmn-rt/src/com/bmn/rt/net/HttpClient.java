package com.bmn.rt.net;

import java.io.IOException;
import java.net.*;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19.
 */
public class HttpClient {
    public static void main(String[] args) throws IOException {


        String url = "http://www.baidu.com";
        CookieManager manager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(manager);

        URL urls = new URL(url);
        URLConnection connection = urls.openConnection();
        Object object = connection.getContent();
        System.out.println(object);

        CookieStore cookieStore = manager.getCookieStore();
        List<HttpCookie> cookieList = cookieStore.getCookies();
        for (HttpCookie c : cookieList) {
            System.out.println(c);
        }

    }
}

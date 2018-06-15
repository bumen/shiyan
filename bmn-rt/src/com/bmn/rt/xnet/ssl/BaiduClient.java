package com.bmn.rt.xnet.ssl;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2017/5/26.
 */
public class BaiduClient {


    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, KeyManagementException {
        String ip = "220.181.111.188:443";
        String host = "https://www.baidu.com/";

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, null, null);

        SSLSocketFactory ssf = context.getSocketFactory();

        SSLSocket socket = (SSLSocket) ssf.createSocket("220.181.111.188", 443);

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String line = reader.readLine();
        if(line != null) {
            System.out.println(line);
        }

        reader.close();

        socket.close();
        System.out.println("访问结束");
    }
}

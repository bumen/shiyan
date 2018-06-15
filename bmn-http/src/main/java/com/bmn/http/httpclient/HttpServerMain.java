package com.bmn.http.httpclient;

import org.apache.http.config.SocketConfig;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.bootstrap.ServerBootstrap;

import java.io.IOException;

/**
 * Created by Administrator on 2017/4/7.
 */
public class HttpServerMain {

    public static void main(String[] args) {
        ServerBootstrap bootstrap = ServerBootstrap.bootstrap();
        bootstrap.setListenerPort(8080).registerHandler("*", new ServerHandler());
        bootstrap.setSocketConfig(
                SocketConfig.custom()
                        .setSoReuseAddress(true)
                        .setSoLinger(3000)
                        .build()
        ).setExceptionLogger(new ErrorLogger())
        ;
        HttpServer server = bootstrap.create();

        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("server start");
    }
}

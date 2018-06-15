package com.bmn.http.httpclient.impl.server;

import org.apache.http.HttpException;
import org.apache.http.HttpServerConnection;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpService;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * Created by Administrator on 2017/5/24.
 */
public class Worker implements Runnable {
    private final HttpService httpService;
    private final HttpServerConnection conn;
    private final Logger exceptionLogger;


    public Worker(HttpService httpService, HttpServerConnection conn, Logger exceptionLogger) {
        this.httpService = httpService;
        this.conn = conn;
        this.exceptionLogger = exceptionLogger;
    }

    public HttpServerConnection getConnection() {return conn;}

    @Override
    public void run() {
        try {
            final BasicHttpContext localContext = new BasicHttpContext();
            final HttpCoreContext context = HttpCoreContext.adapt(localContext);
            while(!Thread.interrupted() && this.conn.isOpen()) {
                this.httpService.handleRequest(conn, context);
                localContext.clear();
            }
            this.conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                this.conn.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

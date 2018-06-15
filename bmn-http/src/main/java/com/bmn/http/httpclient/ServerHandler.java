package com.bmn.http.httpclient;

import org.apache.http.*;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpRequestHandler;

import java.io.IOException;

import static org.apache.http.protocol.HttpCoreContext.HTTP_CONNECTION;

/**
 * Created by Administrator on 2017/4/10.
 */
public class ServerHandler implements HttpRequestHandler {
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        System.out.println("handle begin...");
        HttpServerConnection conn = (HttpServerConnection)context.getAttribute(HTTP_CONNECTION);
        if(conn != null) {
            RequestLine line = request.getRequestLine();
            System.out.println(conn.toString() + "----" + line.toString());
        }
        System.out.println("handle end");


    }
}

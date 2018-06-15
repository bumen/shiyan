package com.bmn.http.httpclient.client;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * Created by Administrator on 2017/6/1.
 */
public class HttpIoClient {

    public static void main(String[] args) throws IOException {

        CloseableHttpClient client =  HttpClients.createDefault();

        HttpGet get = new HttpGet("https://localhost:8088");
        CloseableHttpResponse response = client.execute(get);
}
}

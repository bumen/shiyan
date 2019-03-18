package com.bmn.web.reactive.function.client;

import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author: zyq
 * @date: 2019/3/16
 */
public class BmnWebClient {

    private static final WebClient webClient = WebClient.create("http://localhost:11011");

    public void simpleWebClient() {


    }


}

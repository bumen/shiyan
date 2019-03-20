package com.bmn;

import org.junit.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author: zyq
 * @date: 2019/3/16
 */


public class WebClientTest {

    private static final WebClient webClient = WebClient.create("http://localhost:11011");

    @Test
    public void simpleTest() throws InterruptedException {

        request();

        System.out.println("------------------------------------");

        //request();

        //Thread.sleep(5000);

        //request();
    }


    private void request() {
        Mono<String> mono = webClient.get().uri("hello").retrieve().bodyToMono(String.class).log();

        String v = mono.block();

        System.out.println(v);
    }


}
